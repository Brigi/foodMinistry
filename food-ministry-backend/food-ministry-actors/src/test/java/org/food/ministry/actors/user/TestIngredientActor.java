package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.ingredient.IngredientActor;
import org.food.ministry.actors.ingredient.messages.GetIngredientMessage;
import org.food.ministry.actors.ingredient.messages.GetIngredientResultMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Unit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;

@RunWith(MockitoJUnitRunner.class)
public class TestIngredientActor {

    private static final long INGREDIENT_ID = 0;
    private static final String INGREDIENT_NAME = "MyIngredient";
    private static final Unit INGREDIENT_UNIT = Unit.KILOGRAMM;
    private static final boolean INGREDIENT_IS_BASIC = false;

    @Mock
    private IngredientDAO ingredientDao;

    private Ingredient testIngredient;
    private ActorSystem system;
    private ActorRef ingredientActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        ingredientActor = system.actorOf(IngredientActor.props(ingredientDao), "ingredient-actor");
        testIngredient = new Ingredient(INGREDIENT_ID, INGREDIENT_NAME, INGREDIENT_UNIT, INGREDIENT_IS_BASIC);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Ingredient section ---------------

    @Test
    public void testSuccessfulGetIngredient() throws DataAccessException {
        Mockito.when(ingredientDao.get(INGREDIENT_ID)).thenReturn(testIngredient);

        long messageId = IDGenerator.getRandomID();
        ingredientActor.tell(new GetIngredientMessage(messageId, INGREDIENT_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientResultMessage getIngredientResultMessage = MessageUtil.getMessageByClass(GetIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientResultMessage, delegateResultMessage);
        Assert.assertEquals(INGREDIENT_NAME, getIngredientResultMessage.getIngredientName());
        Assert.assertEquals(INGREDIENT_UNIT, getIngredientResultMessage.getIngredientUnit());
        Assert.assertEquals(INGREDIENT_IS_BASIC, getIngredientResultMessage.isBasicIngredient());
    }

    @Test
    public void testGetIngredientsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(ingredientDao).get(INGREDIENT_ID);

        long messageId = IDGenerator.getRandomID();
        ingredientActor.tell(new GetIngredientMessage(messageId, INGREDIENT_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientResultMessage getIngredientResultMessage = MessageUtil.getMessageByClass(GetIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting ingredient with id {0} failed: {1}", INGREDIENT_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getIngredientResultMessage, delegateMessage);
        Assert.assertEquals("", getIngredientResultMessage.getIngredientName());
        Assert.assertEquals(Unit.NONE, getIngredientResultMessage.getIngredientUnit());
        Assert.assertEquals(false, getIngredientResultMessage.isBasicIngredient());
    }
}

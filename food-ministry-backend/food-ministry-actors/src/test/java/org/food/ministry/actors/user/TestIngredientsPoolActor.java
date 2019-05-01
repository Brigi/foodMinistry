package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.Set;

import org.food.ministry.actors.ingredientspool.IngredientsPoolActor;
import org.food.ministry.actors.ingredientspool.messages.AddIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.AddIngredientResultMessage;
import org.food.ministry.actors.ingredientspool.messages.DeleteIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.DeleteIngredientResultMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.IngredientsPool;
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
public class TestIngredientsPoolActor {

    private static final long INGREDIENTS_POOL_ID = 0;

    @Mock
    private IngredientsPoolDAO ingredientsPoolDao;
    @Mock
    private IngredientDAO ingredientDao;

    private IngredientsPool testIngredientsPool;
    private ActorSystem system;
    private ActorRef ingredientsPoolActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        ingredientsPoolActor = system.actorOf(IngredientsPoolActor.props(ingredientsPoolDao, ingredientDao), "ingredients-pool-actor");
        testIngredientsPool = new IngredientsPool(INGREDIENTS_POOL_ID);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Ingredients Pool section ---------------

    @Test
    public void testSuccessfulGetIngredientsEmptyList() throws DataAccessException {
        Mockito.when(ingredientsPoolDao.get(INGREDIENTS_POOL_ID)).thenReturn(testIngredientsPool);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new GetIngredientsMessage(messageId, INGREDIENTS_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Assert.assertTrue(getIngredientsResultMessage.getIngredientIds().isEmpty());
    }

    @Test
    public void testSuccessfulGetIngredient() throws DataAccessException {
        final long ingredientId = 1;
        final String ingredientName = "MyIngredient";
        testIngredientsPool.addIngredient(new Ingredient(ingredientId, ingredientName, Unit.NONE, false));
        Mockito.when(ingredientsPoolDao.get(INGREDIENTS_POOL_ID)).thenReturn(testIngredientsPool);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new GetIngredientsMessage(messageId, INGREDIENTS_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Set<Long> ingredients = getIngredientsResultMessage.getIngredientIds();
        Assert.assertEquals(1, ingredients.size());
        Assert.assertEquals(ingredientId, ingredients.iterator().next().longValue());
    }

    @Test
    public void testGetIngredientsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(ingredientsPoolDao).get(INGREDIENTS_POOL_ID);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new GetIngredientsMessage(messageId, INGREDIENTS_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting all ingredients of ingredients pool with id {0} failed: {1}", INGREDIENTS_POOL_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getIngredientsResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulAddIngredient() throws DataAccessException {
        Mockito.when(ingredientsPoolDao.get(INGREDIENTS_POOL_ID)).thenReturn(testIngredientsPool);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new AddIngredientMessage(messageId, INGREDIENTS_POOL_ID, "MyIngredient", Unit.NONE, false), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddIngredientResultMessage addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addIngredientResultMessage, delegateResultMessage);
        
        messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new GetIngredientsMessage(messageId, INGREDIENTS_POOL_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Set<Long> ingredients = getIngredientsResultMessage.getIngredientIds();
        Assert.assertEquals(1, ingredients.size());
    }
    
    @Test
    public void testAddIngredientWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(ingredientsPoolDao).get(INGREDIENTS_POOL_ID);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new AddIngredientMessage(messageId, INGREDIENTS_POOL_ID, "MyIngredient", Unit.NONE, false), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddIngredientResultMessage addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Adding an ingredient to ingredients pool with id {0} failed: {1}", INGREDIENTS_POOL_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, addIngredientResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulDeleteIngredient() throws DataAccessException {
        Ingredient ingredient = new Ingredient(1, "MyIngredien", Unit.NONE, false);
        testIngredientsPool.addIngredient(ingredient);
        Mockito.when(ingredientsPoolDao.get(INGREDIENTS_POOL_ID)).thenReturn(testIngredientsPool);
        Mockito.when(ingredientDao.get(ingredient.getId())).thenReturn(ingredient);

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new DeleteIngredientMessage(messageId, INGREDIENTS_POOL_ID, ingredient.getId()), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        DeleteIngredientResultMessage deleteIngredientResultMessage = MessageUtil.getMessageByClass(DeleteIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, deleteIngredientResultMessage, delegateResultMessage);
        
        messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new GetIngredientsMessage(messageId, INGREDIENTS_POOL_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Set<Long> ingredients = getIngredientsResultMessage.getIngredientIds();
        Assert.assertTrue(ingredients.isEmpty());
    }

    @Test
    public void testDeleteIngredientWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(ingredientsPoolDao).get(INGREDIENTS_POOL_ID);
        final long ingredientId = 0;

        long messageId = IDGenerator.getRandomID();
        ingredientsPoolActor.tell(new DeleteIngredientMessage(messageId, INGREDIENTS_POOL_ID, ingredientId), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        DeleteIngredientResultMessage deleteIngredientResultMessage = MessageUtil.getMessageByClass(DeleteIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Deleting of ingredient with {0} from ingredients pool with id {1} failed: {2}", INGREDIENTS_POOL_ID, ingredientId, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, deleteIngredientResultMessage, delegateMessage);
    }
}

package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.recipe.RecipeActor;
import org.food.ministry.actors.recipe.messages.GetRecipeMessage;
import org.food.ministry.actors.recipe.messages.GetRecipeResultMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Recipe;
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
public class TestRecipeActor {

    private static final long RECIPE_ID = 0;
    private static final String RECIPE_NAME = "MyRecipe";
    private static final Map<Ingredient, Float> RECIPE_INGREDIENTS = new HashMap<>();
    private static final String RECIPE_DESCRIPTION = "MyDescription";
    private static final long INGREDIENT_ID = 1;
    private static final String INGREDIENT_NAME = "MyIngredient";
    private static final Unit INGREDIENT_UNIT = Unit.KILOGRAMM;
    private static final boolean INGREDIENT_IS_BASIC = false;

    @Mock
    private RecipeDAO recipeDao;

    private Ingredient testIngredient;
    private Recipe testRecipe;
    private ActorSystem system;
    private ActorRef recipeActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        recipeActor = system.actorOf(RecipeActor.props(recipeDao), "recipe-actor");
        testIngredient = new Ingredient(INGREDIENT_ID, INGREDIENT_NAME, INGREDIENT_UNIT, INGREDIENT_IS_BASIC);
        RECIPE_INGREDIENTS.put(testIngredient, 2.0f);
        testRecipe = new Recipe(RECIPE_ID, RECIPE_NAME, RECIPE_INGREDIENTS, RECIPE_DESCRIPTION);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Ingredient section ---------------

    @Test
    public void testSuccessfulGetRecipe() throws DataAccessException {
        Mockito.when(recipeDao.get(RECIPE_ID)).thenReturn(testRecipe);

        long messageId = IDGenerator.getRandomID();
        recipeActor.tell(new GetRecipeMessage(messageId, RECIPE_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipeResultMessage getRecipeResultMessage = MessageUtil.getMessageByClass(GetRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getRecipeResultMessage, delegateResultMessage);
        Assert.assertEquals(RECIPE_NAME, getRecipeResultMessage.getRecipeName());
        Assert.assertEquals(RECIPE_INGREDIENTS.size(), getRecipeResultMessage.getIngredientsWithAmount().size());
        Assert.assertEquals(RECIPE_INGREDIENTS.get(testIngredient), getRecipeResultMessage.getIngredientsWithAmount().get(INGREDIENT_ID));
        Assert.assertEquals(RECIPE_DESCRIPTION, getRecipeResultMessage.getRecipeDescription());
    }

    @Test
    public void testGetIngredientsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(recipeDao).get(RECIPE_ID);

        long messageId = IDGenerator.getRandomID();
        recipeActor.tell(new GetRecipeMessage(messageId, RECIPE_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipeResultMessage getRecipeResultMessage = MessageUtil.getMessageByClass(GetRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting recipe with id {0} failed: {1}", RECIPE_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getRecipeResultMessage, delegateMessage);
        Assert.assertEquals("", getRecipeResultMessage.getRecipeName());
        Assert.assertTrue(getRecipeResultMessage.getIngredientsWithAmount().isEmpty());
        Assert.assertEquals("", getRecipeResultMessage.getRecipeDescription());
    }
}

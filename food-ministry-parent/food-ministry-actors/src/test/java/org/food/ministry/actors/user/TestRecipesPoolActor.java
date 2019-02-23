package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.recipespool.RecipesPoolActor;
import org.food.ministry.actors.recipespool.messages.AddRecipeMessage;
import org.food.ministry.actors.recipespool.messages.AddRecipeResultMessage;
import org.food.ministry.actors.recipespool.messages.DeleteRecipeMessage;
import org.food.ministry.actors.recipespool.messages.DeleteRecipeResultMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesResultMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.RecipesPool;
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
public class TestRecipesPoolActor {

    private static final long RECIPES_POOL_ID = 0;

    @Mock
    private RecipesPoolDAO recipesPoolDao;
    @Mock
    private RecipeDAO recipeDao;
    @Mock
    private IngredientDAO ingredientDao;

    private RecipesPool testRecipesPool;
    private ActorSystem system;
    private ActorRef recipesPoolActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        recipesPoolActor = system.actorOf(RecipesPoolActor.props(recipesPoolDao, recipeDao, ingredientDao), "recipes-pool-actor");
        testRecipesPool = new RecipesPool(RECIPES_POOL_ID);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Recipes Pool section ---------------

    @Test
    public void testSuccessfulGetRecipesEmptyList() throws DataAccessException {
        Mockito.when(recipesPoolDao.get(RECIPES_POOL_ID)).thenReturn(testRecipesPool);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new GetRecipesMessage(messageId, RECIPES_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipesResultMessage getRecipesResultMessage = MessageUtil.getMessageByClass(GetRecipesResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getRecipesResultMessage, delegateResultMessage);
        Assert.assertTrue(getRecipesResultMessage.getRecipesIdsWithName().isEmpty());
    }

    @Test
    public void testSuccessfulGetRecipes() throws DataAccessException {
        final long recipeId = 0;
        final String recipeName = "MyRecipe";
        testRecipesPool.addRecipe(new Recipe(recipeId, recipeName, new HashMap<>(), "MyDescription"));
        Mockito.when(recipesPoolDao.get(RECIPES_POOL_ID)).thenReturn(testRecipesPool);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new GetRecipesMessage(messageId, RECIPES_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipesResultMessage getRecipesResultMessage = MessageUtil.getMessageByClass(GetRecipesResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getRecipesResultMessage, delegateResultMessage);
        Map<Long, String> recipes = getRecipesResultMessage.getRecipesIdsWithName();
        Assert.assertEquals(1, recipes.size());
        Assert.assertTrue(recipes.containsKey(recipeId));
        Assert.assertEquals(recipeName, recipes.get(recipeId));
    }

    @Test
    public void testGetRecipesWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(recipesPoolDao).get(RECIPES_POOL_ID);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new GetRecipesMessage(messageId, RECIPES_POOL_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipesResultMessage getRecipesResultMessage = MessageUtil.getMessageByClass(GetRecipesResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting all recipes of recipes pool with id {0} failed: {1}", RECIPES_POOL_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getRecipesResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulAddRecipeWithoutIngredients() throws DataAccessException {
        Mockito.when(recipesPoolDao.get(RECIPES_POOL_ID)).thenReturn(testRecipesPool);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new AddRecipeMessage(messageId, RECIPES_POOL_ID, "MyRecipe", new HashMap<>(), "MyDescription"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddRecipeResultMessage addRecipeResultMessage = MessageUtil.getMessageByClass(AddRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addRecipeResultMessage, delegateResultMessage);
    }

    @Test
    public void testSuccessfulAddRecipeWithIngredients() throws DataAccessException {
        Ingredient tomato = new Ingredient(0, "Tomato", Unit.NONE, false);
        Ingredient salt = new Ingredient(1, "Salt", Unit.TEASPOON, false);
        Map<Long, Float> ingredientsMap = new HashMap<>();
        ingredientsMap.put(tomato.getId(), 1f);
        ingredientsMap.put(salt.getId(), 0.5f);
        Mockito.when(recipesPoolDao.get(RECIPES_POOL_ID)).thenReturn(testRecipesPool);
        Mockito.when(ingredientDao.get(tomato.getId())).thenReturn(tomato);
        Mockito.when(ingredientDao.get(salt.getId())).thenReturn(salt);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new AddRecipeMessage(messageId, RECIPES_POOL_ID, "MyRecipe", ingredientsMap, "MyDescription"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddRecipeResultMessage addRecipeResultMessage = MessageUtil.getMessageByClass(AddRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addRecipeResultMessage, delegateResultMessage);
    }

    @Test
    public void testAddRecipeWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(recipesPoolDao).get(RECIPES_POOL_ID);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new AddRecipeMessage(messageId, RECIPES_POOL_ID, "MyRecipe", new HashMap<>(), "MyDescription"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddRecipeResultMessage addRecipeResultMessage = MessageUtil.getMessageByClass(AddRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Adding a recipe to recipes pool with id {0} failed: {1}", RECIPES_POOL_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, addRecipeResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulDeleteRecipe() throws DataAccessException {
        Recipe recipe = new Recipe(0, "MyRecipe", new HashMap<>(), "MyDescription");
        Mockito.when(recipesPoolDao.get(RECIPES_POOL_ID)).thenReturn(testRecipesPool);
        Mockito.when(recipeDao.get(recipe.getId())).thenReturn(recipe);

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new DeleteRecipeMessage(messageId, RECIPES_POOL_ID, recipe.getId()), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        DeleteRecipeResultMessage deleteRecipeResultMessage = MessageUtil.getMessageByClass(DeleteRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, deleteRecipeResultMessage, delegateResultMessage);
    }

    @Test
    public void testDeleteRecipeWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(recipesPoolDao).get(RECIPES_POOL_ID);
        final long recipeId = 0;

        long messageId = IDGenerator.getRandomID();
        recipesPoolActor.tell(new DeleteRecipeMessage(messageId, RECIPES_POOL_ID, recipeId), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        DeleteRecipeResultMessage deleteRecipeResultMessage = MessageUtil.getMessageByClass(DeleteRecipeResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Deleting of recipe with {0} from recipes pool with id {1} failed: {2}", RECIPES_POOL_ID, recipeId, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, deleteRecipeResultMessage, delegateMessage);
    }
}

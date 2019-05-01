package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.household.HouseholdActor;
import org.food.ministry.actors.household.messages.GetFoodInventoryMessage;
import org.food.ministry.actors.household.messages.GetFoodInventoryResultMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolResultMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolResultMessage;
import org.food.ministry.actors.household.messages.GetShoppingListMessage;
import org.food.ministry.actors.household.messages.GetShoppingListResultMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
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
public class TestHouseholdActor {

    private static final long HOUSEHOLD_ID = 0;
    private static final String HOUSEHOLD_NAME = "MyHousehold";
    private static final long FOOD_INVENTORY_ID = 1;
    private static final long INGREDIENTS_POOL_ID = 2;
    private static final long RECIPES_POOL_ID = 3;
    private static final long SHOPPING_LIST_ID = 4;

    @Mock
    private HouseholdDAO householdDao;

    private Household testHousehold;
    private FoodInventory testFoodInventory;
    private IngredientsPool testIngredientsPool;
    private RecipesPool testRecipesPool;
    private ShoppingList testShoppingList;
    private ActorSystem system;
    private ActorRef householdActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        householdActor = system.actorOf(HouseholdActor.props(householdDao), "household-actor");
        testFoodInventory = new FoodInventory(FOOD_INVENTORY_ID);
        testShoppingList = new ShoppingList(SHOPPING_LIST_ID);
        testIngredientsPool = new IngredientsPool(INGREDIENTS_POOL_ID);
        testRecipesPool = new RecipesPool(RECIPES_POOL_ID);
        testHousehold = new Household(HOUSEHOLD_ID, testFoodInventory, testShoppingList, testIngredientsPool, testRecipesPool, HOUSEHOLD_NAME);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Household section ---------------

    @Test
    public void testSuccessfulGetFoodInventory() throws DataAccessException {
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(testHousehold);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetFoodInventoryMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetFoodInventoryResultMessage getFoodInventoryResultMessage = MessageUtil.getMessageByClass(GetFoodInventoryResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getFoodInventoryResultMessage, delegateResultMessage);
        Assert.assertEquals(FOOD_INVENTORY_ID, getFoodInventoryResultMessage.getFoodInventoryId());
    }

    @Test
    public void testGetFoodInventoryWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(householdDao).get(HOUSEHOLD_ID);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetFoodInventoryMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetFoodInventoryResultMessage getIngredientsPoolResultMessage = MessageUtil.getMessageByClass(GetFoodInventoryResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting the food inventory of household with id {0} failed: {1}", HOUSEHOLD_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getIngredientsPoolResultMessage, delegateMessage);
    }
    
    @Test
    public void testSuccessfulGetIngredientsPool() throws DataAccessException {
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(testHousehold);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetIngredientsPoolMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsPoolResultMessage getIngredientsPoolResultMessage = MessageUtil.getMessageByClass(GetIngredientsPoolResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsPoolResultMessage, delegateResultMessage);
        Assert.assertEquals(INGREDIENTS_POOL_ID, getIngredientsPoolResultMessage.getIngredientsPoolId());
    }

    @Test
    public void testGetIngredientsPoolWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(householdDao).get(HOUSEHOLD_ID);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetIngredientsPoolMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsPoolResultMessage getIngredientsPoolResultMessage = MessageUtil.getMessageByClass(GetIngredientsPoolResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting the ingredients pool of household with id {0} failed: {1}", HOUSEHOLD_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getIngredientsPoolResultMessage, delegateMessage);
    }
    
    @Test
    public void testSuccessfulGetRecipesPool() throws DataAccessException {
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(testHousehold);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetRecipesPoolMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipesPoolResultMessage getRecipesPoolResultMessage = MessageUtil.getMessageByClass(GetRecipesPoolResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getRecipesPoolResultMessage, delegateResultMessage);
        Assert.assertEquals(RECIPES_POOL_ID, getRecipesPoolResultMessage.getRecipesPoolId());
    }

    @Test
    public void testGetRecipesPoolWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(householdDao).get(HOUSEHOLD_ID);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetRecipesPoolMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetRecipesPoolResultMessage getRecipesPoolResultMessage = MessageUtil.getMessageByClass(GetRecipesPoolResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting the recipes pool of household with id {0} failed: {1}", HOUSEHOLD_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getRecipesPoolResultMessage, delegateMessage);
    }
    
    @Test
    public void testSuccessfulGetShoppingList() throws DataAccessException {
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(testHousehold);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetShoppingListMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetShoppingListResultMessage getShoppingListResultMessage = MessageUtil.getMessageByClass(GetShoppingListResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getShoppingListResultMessage, delegateResultMessage);
        Assert.assertEquals(SHOPPING_LIST_ID, getShoppingListResultMessage.getShoppingListId());
    }

    @Test
    public void testGetShoppingListWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(householdDao).get(HOUSEHOLD_ID);

        long messageId = IDGenerator.getRandomID();
        householdActor.tell(new GetShoppingListMessage(messageId, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetShoppingListResultMessage getShoppingListResultMessage = MessageUtil.getMessageByClass(GetShoppingListResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting the shopping list of household with id {0} failed: {1}", HOUSEHOLD_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getShoppingListResultMessage, delegateMessage);
    }
}

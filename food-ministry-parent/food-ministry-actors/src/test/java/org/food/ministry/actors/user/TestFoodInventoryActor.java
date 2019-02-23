package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.Map;

import org.food.ministry.actors.foodinventory.FoodInventoryActor;
import org.food.ministry.actors.foodinventory.messages.AddIngredientMessage;
import org.food.ministry.actors.foodinventory.messages.AddIngredientResultMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.model.FoodInventory;
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
public class TestFoodInventoryActor {

    private static final long FOOD_INVENTORY_ID = 0;
    private static final long INGREDIENT_ID = 1;
    private static final String INGREDIENT_NAME = "MyIngredient";
    private static final Unit INGREDIENT_UNIT = Unit.KILOGRAMM;
    private static final boolean INGREDIENT_IS_BASIC = false;

    @Mock
    private FoodInventoryDAO foodInventoryDao;
    @Mock
    private IngredientDAO ingredientDao;

    private FoodInventory testFoodInventory;
    private Ingredient testIngredient;
    private ActorSystem system;
    private ActorRef foodInventoryActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        foodInventoryActor = system.actorOf(FoodInventoryActor.props(foodInventoryDao, ingredientDao), "food-inventory-actor");
        testFoodInventory = new FoodInventory(FOOD_INVENTORY_ID);
        testIngredient = new Ingredient(INGREDIENT_ID, INGREDIENT_NAME, INGREDIENT_UNIT, INGREDIENT_IS_BASIC);
    }

    @After
    public void teardown() {
        system.terminate();
    }

    // --------------- Food Inventory section ---------------

    @Test
    public void testSuccessfulGetIngredientsWithEmptyList() throws DataAccessException {
        Mockito.when(foodInventoryDao.get(FOOD_INVENTORY_ID)).thenReturn(testFoodInventory);

        long messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new GetIngredientsMessage(messageId, FOOD_INVENTORY_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Assert.assertTrue(getIngredientsResultMessage.getIngredientsWithAmount().isEmpty());
    }

    @Test
    public void testGetIngredientsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(foodInventoryDao).get(FOOD_INVENTORY_ID);

        long messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new GetIngredientsMessage(messageId, FOOD_INVENTORY_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting all ingredients of the food inventory with id {0} failed: {1}", FOOD_INVENTORY_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getIngredientsResultMessage, delegateMessage);
        Assert.assertTrue(getIngredientsResultMessage.getIngredientsWithAmount().isEmpty());
    }
    
    @Test
    public void testSuccessfulAddIngredient() throws DataAccessException {
        Mockito.when(foodInventoryDao.get(FOOD_INVENTORY_ID)).thenReturn(testFoodInventory);
        Mockito.when(ingredientDao.get(INGREDIENT_ID)).thenReturn(testIngredient);

        long messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new AddIngredientMessage(messageId, FOOD_INVENTORY_ID, INGREDIENT_ID, 2), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddIngredientResultMessage addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addIngredientResultMessage, delegateResultMessage);
    }

    @Test
    public void testAddIngredientWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(foodInventoryDao).get(FOOD_INVENTORY_ID);

        long messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new AddIngredientMessage(messageId, FOOD_INVENTORY_ID, INGREDIENT_ID, 2), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddIngredientResultMessage addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Adding/Removing an ingredient to/from food inventory with id {0} failed: {1}", FOOD_INVENTORY_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, addIngredientResultMessage, delegateMessage);
    }
    
    @Test
    public void testSuccessfulAddGetRemoveIngredient() throws DataAccessException {
        Mockito.when(foodInventoryDao.get(FOOD_INVENTORY_ID)).thenReturn(testFoodInventory);
        Mockito.when(ingredientDao.get(INGREDIENT_ID)).thenReturn(testIngredient);

        // Add ingredient
        float amount = 2.0f;
        long messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new AddIngredientMessage(messageId, FOOD_INVENTORY_ID, INGREDIENT_ID, amount), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddIngredientResultMessage addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addIngredientResultMessage, delegateResultMessage);
        
        // Check amount of ingredient
        messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new GetIngredientsMessage(messageId, FOOD_INVENTORY_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetIngredientsResultMessage getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        Map<Long, Float> ingredientToAmountMap = getIngredientsResultMessage.getIngredientsWithAmount();
        Assert.assertEquals(1, ingredientToAmountMap.size());
        Assert.assertEquals(amount, ingredientToAmountMap.get(INGREDIENT_ID), 0.00001);
        
        // Remove ingredient
        messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new AddIngredientMessage(messageId, FOOD_INVENTORY_ID, INGREDIENT_ID, 0 - amount), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        addIngredientResultMessage = MessageUtil.getMessageByClass(AddIngredientResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addIngredientResultMessage, delegateResultMessage);
        
        // Check amount of ingredient
        messageId = IDGenerator.getRandomID();
        foodInventoryActor.tell(new GetIngredientsMessage(messageId, FOOD_INVENTORY_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        getIngredientsResultMessage = MessageUtil.getMessageByClass(GetIngredientsResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getIngredientsResultMessage, delegateResultMessage);
        ingredientToAmountMap = getIngredientsResultMessage.getIngredientsWithAmount();
        Assert.assertEquals(0, ingredientToAmountMap.size());
    }
}

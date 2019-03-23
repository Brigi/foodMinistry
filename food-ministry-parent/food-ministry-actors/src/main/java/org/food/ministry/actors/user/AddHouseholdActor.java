package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.user.messages.AddHouseholdMessage;
import org.food.ministry.actors.user.messages.AddHouseholdResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
import org.food.ministry.model.User;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for adding households to a user.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class AddHouseholdActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for users
     */
    private UserDAO userDao;
    /**
     * The data access object for households
     */
    private HouseholdDAO householdDao;
    /**
     * The data access object for food inventories
     */
    private FoodInventoryDAO foodInventoryDao;
    /**
     * The data access object for shopping lists
     */
    private ShoppingListDAO shoppingListDao;
    /**
     * The data access object for recipe pools
     */
    private RecipesPoolDAO recipesPoolDao;
    /**
     * The data access object for ingredients pools
     */
    private IngredientsPoolDAO ingredientsPoolDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param userDao The data access object for users
     * @param householdDao The data access object for households
     * @param foodInventoryDao The data access object for food inventories
     * @param shoppingListDao The data access object for shopping lists
     * @param ingredientsPoolDao The data access object for ingredients pools
     */
    public AddHouseholdActor(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao, RecipesPoolDAO recipesPoolDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        this.userDao = userDao;
        this.householdDao = householdDao;
        this.foodInventoryDao = foodInventoryDao;
        this.shoppingListDao = shoppingListDao;
        this.recipesPoolDao = recipesPoolDao;
        this.ingredientsPoolDao = ingredientsPoolDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param userDao The data access object for users
     * @param householdDao The data access object for households
     * @param foodInventoryDao The data access object for food inventories
     * @param shoppingListDao The data access object for shopping lists
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao, RecipesPoolDAO recipesPoolDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        return Props.create(AddHouseholdActor.class, () -> new AddHouseholdActor(userDao, householdDao, foodInventoryDao, shoppingListDao, recipesPoolDao, ingredientsPoolDao));
    }

    /**
     * Accepts a {@link AddHouseholdMessage} and tries to add a household to the
     * user with the given information from the message. Afterwards a
     * {@link AddHouseholdResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(AddHouseholdMessage.class, this::addHousehold);

        return receiveBuilder.build();
    }

    /**
     * Tries to add a household for the user contained in provided the message with
     * the contained user id
     * 
     * @param message The message containing all needed information for adding a
     *            household for a user
     */
    private void addHousehold(AddHouseholdMessage message) {
        long userId = message.getUserId();
        LOGGER.info("Adding a new household for user with id {}", userId);
        try {
            User user = userDao.get(userId);
            long householdId = UtilFunctions.generateUniqueId(householdDao, LOGGER);
            long foodInventoryId = UtilFunctions.generateUniqueId(foodInventoryDao, LOGGER);
            long shoppingListId = UtilFunctions.generateUniqueId(shoppingListDao, LOGGER);
            long ingredientsPoolId = UtilFunctions.generateUniqueId(ingredientsPoolDao, LOGGER);
            long recipesPoolId = UtilFunctions.generateUniqueId(ingredientsPoolDao, LOGGER);

            FoodInventory foodInventory = new FoodInventory(foodInventoryId);
            ShoppingList shoppingList = new ShoppingList(shoppingListId);
            IngredientsPool ingredientsPool = new IngredientsPool(ingredientsPoolId);
            RecipesPool recipesPool = new RecipesPool(recipesPoolId);
            Household household = new Household(householdId, foodInventory, shoppingList, ingredientsPool, recipesPool, message.getName());
            user.addHousehold(household);
            foodInventoryDao.save(foodInventory);
            ingredientsPoolDao.save(ingredientsPool);
            recipesPoolDao.save(recipesPool);
            shoppingListDao.save(shoppingList);
            householdDao.save(household);            
            userDao.update(user);
            AddHouseholdResultMessage resultMessage = new AddHouseholdResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE);
            getSender().tell(resultMessage, getSelf());
            LOGGER.info("Successfully added a household for user with id {}", userId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting households for user with id {0} failed: {1}", userId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new AddHouseholdResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

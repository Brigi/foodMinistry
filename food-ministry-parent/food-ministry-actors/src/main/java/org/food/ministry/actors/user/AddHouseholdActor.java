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

public class AddHouseholdActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    private UserDAO userDao;
    private HouseholdDAO householdDao;
    private FoodInventoryDAO foodInventoryDao;
    private ShoppingListDAO shoppingListDao;
    private IngredientsPoolDAO ingredientsPoolDao;

    public AddHouseholdActor(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        this.userDao = userDao;
        this.householdDao = householdDao;
        this.foodInventoryDao = foodInventoryDao;
        this.shoppingListDao = shoppingListDao;
        this.ingredientsPoolDao = ingredientsPoolDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        return Props.create(AddHouseholdActor.class, () -> new AddHouseholdActor(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao));
    }

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
     * @param message
     *            The message containing all needed information for adding a
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

            user.addHousehold(new Household(householdId, new FoodInventory(foodInventoryId), new ShoppingList(shoppingListId), new IngredientsPool(ingredientsPoolId),
                    new RecipesPool(recipesPoolId), message.getName()));
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

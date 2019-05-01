package org.food.ministry.actors.foodinventory;

import java.text.MessageFormat;

import org.food.ministry.actors.foodinventory.messages.AddIngredientMessage;
import org.food.ministry.actors.foodinventory.messages.AddIngredientResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Ingredient;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for adding ingredients to an ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddIngredientActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for food inventories
     */
    private FoodInventoryDAO foodInventoryDao;
    
    /**
     * The data access object for ingredients
     */
    private IngredientDAO ingredientDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param foodInventoryDao The data access object for food inventories
     * @param ingredientDao The data access object for ingredients
     */
    public AddIngredientActor(FoodInventoryDAO foodInventoryDao, IngredientDAO ingredientDao) {
        this.foodInventoryDao = foodInventoryDao;
        this.ingredientDao = ingredientDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param foodInventoryDao The data access object for food inventories
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(FoodInventoryDAO foodInventoryDao, IngredientDAO ingredientDao) {
        return Props.create(AddIngredientActor.class, () -> new AddIngredientActor(foodInventoryDao, ingredientDao));
    }

    /**
     * Accepts a {@link AddIngredientMessage} and tries to add/remove an ingredient to/from the food inventory
     * with the given information from the message. Afterwards a
     * {@link AddIngredientResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(AddIngredientMessage.class, this::addIngredient);

        return receiveBuilder.build();
    }

    /**
     * Tries to add/remove an ingredient to/from the food inventory contained in provided the message
     * with the contained food inventory id
     * 
     * @param message The message containing all needed information for adding/removing an
     *            ingredient to/from a food inventory
     */
    private void addIngredient(AddIngredientMessage message) {
        long foodInventoryId = message.getFoodInventoryId();
        LOGGER.info("Adding/Removing an ingredient to/from food inventory with id {}", foodInventoryId);
        try {
            FoodInventory foodInventory = foodInventoryDao.get(foodInventoryId);
            Ingredient ingredient = ingredientDao.get(message.getIngredientId());
            foodInventory.addIngredient(ingredient, message.getIngredientAmount());
            foodInventoryDao.update(foodInventory);
            getSender().tell(new AddIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully added/removed an ingredient to food/from the food inventory with id {}", foodInventoryId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Adding/Removing an ingredient to/from food inventory with id {0} failed: {1}", foodInventoryId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new AddIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

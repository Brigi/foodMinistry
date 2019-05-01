package org.food.ministry.actors.foodinventory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.food.ministry.actors.foodinventory.messages.GetIngredientsMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.model.FoodInventory;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for getting all ingredients from an food inventory.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for food inventories
     */
    private FoodInventoryDAO foodInventoryDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param foodInventoryDao The data access object for food inventories
     */
    public GetIngredientsActor(FoodInventoryDAO foodInventoryDao) {
        this.foodInventoryDao = foodInventoryDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param foodInventoryDao The data access object for food inventories
     * @return The property for creating an actor of this class
     */
    public static Props props(FoodInventoryDAO foodInventoryDao) {
        return Props.create(GetIngredientsActor.class, () -> new GetIngredientsActor(foodInventoryDao));
    }

    /**
     * Accepts a {@link GetIngredientsMessage} and tries to get all ingredients from the
     * food inventory with the given information from the message. Afterwards a
     * {@link GetIngredientsResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientsMessage.class, this::getIngredients);

        return receiveBuilder.build();
    }

    /**
     * Tries to get all ingredients from the food inventory contained in provided the message
     * with the contained food inventory id
     * 
     * @param message The message containing all needed information for getting all
     *            ingredients with their amount from a food inventory
     */
    private void getIngredients(GetIngredientsMessage message) {
        long foodInventoryId = message.getFoodInventoryId();
        LOGGER.info("Getting all ingredients of the food inventory with id {}", foodInventoryId);
        try {
            FoodInventory foodInventory = foodInventoryDao.get(foodInventoryId);
            Map<Long, Float> ingredientToAmountMap = foodInventory.getIngredientsWithQuantity().entrySet().parallelStream().collect(Collectors.toMap(element -> element.getKey().getId(), Entry::getValue));
            getSender().tell(new GetIngredientsResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, ingredientToAmountMap), getSelf());
            LOGGER.info("Successfully got all ingredients of the food inventory with id {}", foodInventoryId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting all ingredients of the food inventory with id {0} failed: {1}", foodInventoryId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetIngredientsResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, new HashMap<>()), getSelf());
        }
    }
}

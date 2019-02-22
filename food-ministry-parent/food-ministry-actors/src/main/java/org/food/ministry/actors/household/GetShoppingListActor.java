package org.food.ministry.actors.household;

import java.text.MessageFormat;

import org.food.ministry.actors.household.messages.GetShoppingListMessage;
import org.food.ministry.actors.household.messages.GetShoppingListResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.model.Household;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for getting shopping lists from a household.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class GetShoppingListActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for households
     */
    private HouseholdDAO householdDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param householdDao The data access object for households
     */
    public GetShoppingListActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param householdDao The data access object for households
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetShoppingListActor.class, () -> new GetShoppingListActor(householdDao));
    }

    /**
     * Accepts a {@link GetShoppingListMessage} and tries to get a recipes pool from
     * the household with the given information from the message. Afterwards a
     * {@link GetShoppingListResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetShoppingListMessage.class, this::getShoppingList);

        return receiveBuilder.build();
    }

    /**
     * Tries to get a shopping list from the household contained in provided the
     * message with the contained user id
     * 
     * @param message The message containing all needed information for getting a
     *            shopping list from a household
     */
    private void getShoppingList(GetShoppingListMessage message) {
        long householdId = message.getHouseholdId();
        LOGGER.info("Getting shopping list of household with id {}", householdId);
        try {
            Household household = householdDao.get(householdId);
            long shoppingListId = household.getShoppingList().getId();
            getSender().tell(new GetShoppingListResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, shoppingListId), getSelf());
            LOGGER.info("Successfully got shopping list with id {} of household with id {}", shoppingListId, householdId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting the shopping list of household with id {0} failed: {1}", householdId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetShoppingListResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, 0), getSelf());
        }
    }
}

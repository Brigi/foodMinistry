package org.food.ministry.actors.household;

import java.text.MessageFormat;

import org.food.ministry.actors.household.messages.GetIngredientsPoolMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolResultMessage;
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
 * This actor handles an attempt for getting ingredients pools from a household.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class GetIngredientsPoolActor extends AbstractActor {

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
    public GetIngredientsPoolActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param householdDao The data access object for households
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetIngredientsPoolActor.class, () -> new GetIngredientsPoolActor(householdDao));
    }

    /**
     * Accepts a {@link GetIngredientsPoolMessage} and tries to get a ingredients
     * pool from the household with the given information from the message.
     * Afterwards a {@link GetIngredientsPoolResultMessage} is send back to the
     * requesting actor containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientsPoolMessage.class, this::getIngredientsPool);

        return receiveBuilder.build();
    }

    /**
     * Tries to get a ingredients pool from the household contained in provided the
     * message with the contained user id
     * 
     * @param message The message containing all needed information for getting a
     *            ingredients pool from a household
     */
    private void getIngredientsPool(GetIngredientsPoolMessage message) {
        long householdId = message.getHouseholdId();
        LOGGER.info("Getting ingredients pool of household with id {}", householdId);
        try {
            Household household = householdDao.get(householdId);
            long ingredientsPoolId = household.getIngredientsPool().getId();
            getSender().tell(new GetIngredientsPoolResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, ingredientsPoolId), getSelf());
            LOGGER.info("Successfully got ingredients pool with id {} of household with id {}", ingredientsPoolId, householdId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting the ingredients pool of household with id {0} failed: {1}", householdId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetIngredientsPoolResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, 0), getSelf());
        }
    }
}

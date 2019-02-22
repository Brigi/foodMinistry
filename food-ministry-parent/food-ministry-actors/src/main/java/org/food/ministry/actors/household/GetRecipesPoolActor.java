package org.food.ministry.actors.household;

import java.text.MessageFormat;

import org.food.ministry.actors.household.messages.GetRecipesPoolMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolResultMessage;
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
 * This actor handles an attempt for getting recipes pools from a household.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class GetRecipesPoolActor extends AbstractActor {

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
    public GetRecipesPoolActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param householdDao The data access object for households
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetRecipesPoolActor.class, () -> new GetRecipesPoolActor(householdDao));
    }

    /**
     * Accepts a {@link GetRecipesPoolMessage} and tries to get a recipes pool from
     * the household with the given information from the message. Afterwards a
     * {@link GetRecipesPoolResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesPoolMessage.class, this::getRecipesPool);

        return receiveBuilder.build();
    }

    /**
     * Tries to get a recipes pool from the household contained in provided the
     * message with the contained user id
     * 
     * @param message The message containing all needed information for getting a
     *            recipes pool from a household
     */
    private void getRecipesPool(GetRecipesPoolMessage message) {
        long householdId = message.getHouseholdId();
        LOGGER.info("Getting recipes pool of household with id {}", householdId);
        try {
            Household household = householdDao.get(householdId);
            long recipesPoolId = household.getRecipesPool().getId();
            getSender().tell(new GetRecipesPoolResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, recipesPoolId), getSelf());
            LOGGER.info("Successfully got recipes pool with id {} of household with id {}", recipesPoolId, householdId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting the recipes pool of household with id {0} failed: {1}", householdId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetRecipesPoolResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, 0), getSelf());
        }
    }
}

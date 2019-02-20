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

public class GetIngredientsPoolActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    private HouseholdDAO householdDao;
    
    public GetIngredientsPoolActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetIngredientsPoolActor.class, () -> new GetIngredientsPoolActor(householdDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientsPoolMessage.class, this::getIngredientsPool);

        return receiveBuilder.build();
    }
    
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

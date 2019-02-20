package org.food.ministry.actors.household;

import java.text.MessageFormat;

import org.food.ministry.actors.household.messages.GetFoodInventoryMessage;
import org.food.ministry.actors.household.messages.GetFoodInventoryResultMessage;
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

public class GetFoodInventoryActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    private HouseholdDAO householdDao;
    
    public GetFoodInventoryActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetFoodInventoryActor.class, () -> new GetFoodInventoryActor(householdDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetFoodInventoryMessage.class, this::getFoodInventory);

        return receiveBuilder.build();
    }
    
    private void getFoodInventory(GetFoodInventoryMessage message) {
        long householdId = message.getHouseholdId();
        LOGGER.info("Getting food inventory of household with id {}", householdId);
        try {
            Household household = householdDao.get(householdId);
            long foodInventoryId = household.getFoodInventory().getId();            
            getSender().tell(new GetFoodInventoryResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, foodInventoryId), getSelf());
            LOGGER.info("Successfully got food inventory with id {} of household with id {}", foodInventoryId, householdId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting the food inventory of household with id {0} failed: {1}", householdId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetFoodInventoryResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, 0), getSelf());
        }
    }
}

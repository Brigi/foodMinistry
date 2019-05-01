package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for requesting the id of a food inventory.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetFoodInventoryMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the household requesting the food inventory
     */
    private final long householdId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param householdId The id of the household
     */
    public GetFoodInventoryMessage(long id, long householdId) {
        super(id);
        this.householdId = householdId;
    }

    /**
     * Gets the id of the household
     * 
     * @return The id of the household
     */
    public long getHouseholdId() {
        return householdId;
    }
}

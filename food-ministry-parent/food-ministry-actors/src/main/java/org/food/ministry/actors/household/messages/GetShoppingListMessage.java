package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for requesting the id of a shopping list.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetShoppingListMessage extends AMessage {

    /**
     * The id of the household requesting the shopping list
     */
    private final long householdId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param householdId The id of the household
     */
    public GetShoppingListMessage(long id, long householdId) {
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

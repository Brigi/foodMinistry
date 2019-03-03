package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for removing a household from a user.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class RemoveHouseholdMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the user
     */
    private final long userId;
    /**
     * The id of the household
     */
    private final long householdId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param userId The id of the user
     * @param householdId The id of the household
     */
    public RemoveHouseholdMessage(long id, long userId, long householdId) {
        super(id);
        this.userId = userId;
        this.householdId = householdId;
    }

    /**
     * Gets the id of the user
     * 
     * @return The id of the user
     */
    public long getUserId() {
        return userId;
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

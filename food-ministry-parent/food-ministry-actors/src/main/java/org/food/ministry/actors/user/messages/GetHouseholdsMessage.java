package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for requesting the id of a household.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetHouseholdsMessage extends AMessage {

    /**
     * The id of the recipes pool
     */
    private long userId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param userId The id of the user
     */
    public GetHouseholdsMessage(long id, long userId) {
        super(id);
        this.userId = userId;
    }

    /**
     * Gets the id of the user
     * 
     * @return The id of the user
     */
    public long getUserId() {
        return userId;
    }
}

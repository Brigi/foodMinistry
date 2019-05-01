package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of an attempt to remove a household.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class RemoveHouseholdResultMessage extends AResultMessage {

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     */
    public RemoveHouseholdResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}

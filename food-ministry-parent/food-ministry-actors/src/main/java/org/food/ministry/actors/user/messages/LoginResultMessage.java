package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * A specific kind of {@link AResultMessage} for transmitting the result of a
 * login attempt.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class LoginResultMessage extends AResultMessage {

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     */
    public LoginResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }

}

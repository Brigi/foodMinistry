package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * A specific kind of {@link AResultMessage} for transmitting the result of a login attempt.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class LoginResultMessage extends AResultMessage{

    /**
     * @see AResultMessage#AResultMessage(int, int, boolean, String)
     */
    public LoginResultMessage(int id, int originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
    
}

package org.food.ministry.actors.user.messages;

/**
 * A specific kind of {@link AUserInfoMessage} for transmitting the information
 * for a login attempt.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class LoginMessage extends AUserInfoMessage {

    /**
     * @see AUserInfoMessage#AUserInfoMessage(int, String, String, String)
     */
    public LoginMessage(long id, String username, String emailAddress, String password) {
        super(id, username, emailAddress, password);
    }

}

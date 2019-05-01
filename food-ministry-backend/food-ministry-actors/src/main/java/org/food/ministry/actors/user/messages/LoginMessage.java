package org.food.ministry.actors.user.messages;

/**
 * A specific kind of {@link AUserInfoMessage} for transmitting the information
 * for a login attempt.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class LoginMessage extends AUserInfoMessage {

    /**
     * Constructor initializing the message with essential member variables
     * 
     * @param id The ID of this message
     * @param emailAddress The email address of the user requesting information
     * @param password The password of the user needed for user actions
     */
    public LoginMessage(long id, String emailAddress, String password) {
        super(id, emailAddress, password);
    }

}

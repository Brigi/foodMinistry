package org.food.ministry.actors.user.messages;

/**
 * A specific kind of {@link AUserInfoMessage} for transmitting the information
 * for a registration attempt.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class RegisterMessage extends AUserInfoMessage {

    /**
     * The name of the user requesting information
     */
    private final String username;

    /**
     * Constructor initializing the message with essential member variables
     * 
     * @param id The ID of this message
     * @param username The name of the user requesting information
     * @param emailAddress The email address of the user requesting information
     * @param password The password of the user needed for user actions
     */
    public RegisterMessage(long id, String username, String emailAddress, String password) {
        super(id, emailAddress, password);
        this.username = username;
    }

    /**
     * Gets the name of the user requesting information
     * 
     * @return The name of the user requesting information
     */
    public String getUsername() {
        return username;
    }
}

package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * An abstract implementation of {@link IMessage} for requesting user
 * information
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class AUserInfoMessage extends AMessage implements IRequestMessage {
    
    /**
     * The email address of the user requesting information
     */
    private final String emailAddress;
    /**
     * The password of the user needed for user actions
     */
    private final String password;

    /**
     * Constructor initializing the message with essential member variables
     * 
     * @param id The ID of this message
     * @param emailAddress The email address of the user requesting information
     * @param password The password of the user needed for user actions
     */
    public AUserInfoMessage(long id, String emailAddress, String password) {
        super(id);
        this.emailAddress = emailAddress;
        this.password = password;
    }

    /**
     * Gets the email address of the user requesting information
     * 
     * @return The email address of the user requesting information
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the password of the user needed for user actions
     * 
     * @return The password of the user needed for user actions
     */
    public String getPassword() {
        return password;
    }
}

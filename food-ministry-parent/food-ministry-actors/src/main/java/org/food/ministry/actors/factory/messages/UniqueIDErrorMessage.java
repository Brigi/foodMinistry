package org.food.ministry.actors.factory.messages;

import org.food.ministry.actors.messages.IMessage;

/**
 * The message for indicating an error during the ID generation
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class UniqueIDErrorMessage implements IMessage {

    /**
     * The error message
     */
    private String errorMessage;

    /**
     * Constructor setting the error message
     * 
     * @param errorMessage
     *            The error message
     */
    public UniqueIDErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the error message of the failure for the unique ID generation
     * 
     * @return The error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}

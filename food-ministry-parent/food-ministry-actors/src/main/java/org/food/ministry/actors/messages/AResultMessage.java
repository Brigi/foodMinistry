package org.food.ministry.actors.messages;

/**
 * An abstract implementation of {@link IMessage} for results
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class AResultMessage implements IMessage {

    /**
     * The ID of this message
     */
    private long id;
    /**
     * The ID of the requesting message
     */
    private long originId;
    /**
     * Determines if the requesting message was successfully processed
     */
    private boolean successful;
    /**
     * Error message in case something went wrong
     */
    private String errorMessage;

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id
     *            The ID of the message
     * @param originId
     *            The ID of the requesting message
     * @param successful
     *            Determines if the requesting message was successfully processed
     * @param errorMessage
     *            Error message in case something went wrong
     */
    public AResultMessage(long id, long originId, boolean successful, String errorMessage) {
        this.id = id;
        this.originId = originId;
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the ID of this message
     * 
     * @return The ID of this message
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the ID of the requesting message
     * 
     * @return The ID of the requesting message
     */
    public long getOriginId() {
        return originId;
    }

    /**
     * Determines if the requesting message was successfully processed
     * 
     * @return true, if the requesting message was successfully processed; else
     *         false
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Gets the error message
     * 
     * @return The error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}

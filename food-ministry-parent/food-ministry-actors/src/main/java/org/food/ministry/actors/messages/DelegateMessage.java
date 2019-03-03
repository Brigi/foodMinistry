package org.food.ministry.actors.messages;

/**
 * A message for delegating a request to another actor
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class DelegateMessage extends AMessage implements IMessage {

    /**
     * The ID of the original message
     */
    private final long originId;

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of this message
     * @param originId The ID of the original message
     */
    public DelegateMessage(long id, long originId) {
        super(id);
        this.originId = originId;
    }

    /**
     * Gets the ID of the original message
     * 
     * @return The ID of the original message
     */
    public long getOriginId() {
        return originId;
    }
}

package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.IMessage;

/**
 * A message for delegating a request to another actor
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class DelegateMessage implements IMessage {

    /**
     * The ID of this message
     */
    private long id;
    /**
     * The ID of the original message
     */
    private long originId;

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id
     *            The ID of this message
     * @param originId
     *            The ID of the original message
     */
    public DelegateMessage(long id, long originId) {
        this.id = id;
        this.originId = originId;
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
     * Gets the ID of the original message
     * 
     * @return The ID of the original message
     */
    public long getOriginId() {
        return originId;
    }
}

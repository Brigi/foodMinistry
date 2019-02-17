package org.food.ministry.actors.factory.messages;

import org.food.ministry.actors.messages.IMessage;

/**
 * The message for delivering a requested ID
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class UniqueIDResultMessage implements IMessage {

    /**
     * The ID
     */
    private long nextId;

    /**
     * Constructor setting the ID, which was requested before
     * 
     * @param nextId
     *            the ID
     */
    public UniqueIDResultMessage(long nextId) {
        this.nextId = nextId;
    }

    /**
     * Gets the ID, which was requested before
     * 
     * @return The ID
     */
    public long getNextId() {
        return nextId;
    }
}

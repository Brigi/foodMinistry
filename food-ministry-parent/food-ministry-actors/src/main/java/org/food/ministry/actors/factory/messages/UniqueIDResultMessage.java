package org.food.ministry.actors.factory.messages;

import org.food.ministry.actors.messages.IMessage;

/**
 * The message for delivering a before requested ID
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class UniqueIDResultMessage implements IMessage {

    /**
     * The ID
     */
    private int nextId;
    
    /**
     * Constructor setting the ID, which was requested before
     * @param nextId the ID
     */
    public UniqueIDResultMessage(int nextId) {
        this.nextId = nextId;
    }

    /**
     * Gets the ID, which was requested before
     * @return The ID
     */
    public int getNextId() {
        return nextId;
    }
}

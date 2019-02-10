package org.food.ministry.actors.factory.messages;

import org.food.ministry.actors.IMessage;

public class NextIDResultMessage implements IMessage {

    private int nextId;
    
    public NextIDResultMessage(int nextId) {
        this.nextId = nextId;
    }

    public int getNextId() {
        return nextId;
    }
}

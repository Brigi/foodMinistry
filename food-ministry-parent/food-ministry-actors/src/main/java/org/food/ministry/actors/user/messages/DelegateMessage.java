package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.IMessage;

public class DelegateMessage implements IMessage {
    
    private int id;
    private int originId;
    
    public DelegateMessage(int id, int originId) {
        this.id = id;
        this.originId = originId;
    }

    public int getId() {
        return id;
    }

    public int getOriginId() {
        return originId;
    }
}

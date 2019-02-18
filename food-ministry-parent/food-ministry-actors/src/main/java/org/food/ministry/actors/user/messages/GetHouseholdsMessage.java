package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;

public class GetHouseholdsMessage extends AMessage {

    private long userId;
    
    public GetHouseholdsMessage(long id, long userId) {
        super(id);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}

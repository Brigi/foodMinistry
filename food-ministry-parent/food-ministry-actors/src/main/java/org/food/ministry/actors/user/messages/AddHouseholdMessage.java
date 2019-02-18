package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;

public class AddHouseholdMessage extends AMessage {

    private final long userId;
    
    private final String name;
    
    public AddHouseholdMessage(long id, long userId, String name) {
        super(id);
        this.userId = userId;
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
}

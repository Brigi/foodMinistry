package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;

public class RemoveHouseholdMessage extends AMessage {

    private final long userId;
    
    private final long householdId;
    
    public RemoveHouseholdMessage(long id, long userId, long householdId) {
        super(id);
        this.userId = userId;
        this.householdId = householdId;
    }

    public long getUserId() {
        return userId;
    }
    
    public long getHouseholdId() {
        return householdId;
    }
}

package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AMessage;

public class GetRecipesPoolMessage extends AMessage {

    private final long householdId;
    
    public GetRecipesPoolMessage(long id, long householdId) {
        super(id);
        this.householdId = householdId;
    }

    public long getHouseholdId() {
        return householdId;
    }
}

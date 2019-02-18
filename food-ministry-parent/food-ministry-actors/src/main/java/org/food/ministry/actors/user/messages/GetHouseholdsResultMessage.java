package org.food.ministry.actors.user.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

public class GetHouseholdsResultMessage extends AResultMessage {

    private final Map<Long, String> households;
    
    public GetHouseholdsResultMessage(long id, long originId, boolean successful, String errorMessage, Map<Long, String> households) {
        super(id, originId, successful, errorMessage);
        this.households = households;
    }

    public Map<Long, String> getHouseholdIdsWithName() {
        return households;
    }
}

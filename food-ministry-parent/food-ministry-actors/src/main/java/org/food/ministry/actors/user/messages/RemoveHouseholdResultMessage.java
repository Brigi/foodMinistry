package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class RemoveHouseholdResultMessage extends AResultMessage {
    
    public RemoveHouseholdResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}

package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class AddHouseholdResultMessage extends AResultMessage {
    
    public AddHouseholdResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}

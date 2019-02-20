package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class GetIngredientsPoolResultMessage extends AResultMessage {

    private final long ingredientsPoolId;
    
    public GetIngredientsPoolResultMessage(long id, long originId, boolean successful, String errorMessage, long ingredientsPoolId) {
        super(id, originId, successful, errorMessage);
        this.ingredientsPoolId = ingredientsPoolId;
    }

    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }
}

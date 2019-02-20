package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class GetRecipesPoolResultMessage extends AResultMessage {

    private final long recipesPoolId;
    
    public GetRecipesPoolResultMessage(long id, long originId, boolean successful, String errorMessage, long recipesPoolId) {
        super(id, originId, successful, errorMessage);
        this.recipesPoolId = recipesPoolId;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }
}

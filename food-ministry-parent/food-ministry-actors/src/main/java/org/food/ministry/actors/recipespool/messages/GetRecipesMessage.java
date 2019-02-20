package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AMessage;

public class GetRecipesMessage extends AMessage {

    private final long recipesPoolId;
    
    public GetRecipesMessage(long id, long recipesPoolId) {
        super(id);
        this.recipesPoolId = recipesPoolId;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }
}

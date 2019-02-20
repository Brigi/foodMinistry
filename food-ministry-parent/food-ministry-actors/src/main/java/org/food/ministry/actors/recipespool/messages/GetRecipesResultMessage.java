package org.food.ministry.actors.recipespool.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

public class GetRecipesResultMessage extends AResultMessage {

    private final Map<Long, String> recipes;
    
    public GetRecipesResultMessage(long id, long originId, boolean successful, String errorMessage, Map<Long, String> recipes) {
        super(id, originId, successful, errorMessage);
        this.recipes = recipes;
    }
    
    public Map<Long, String> getRecipesIdsWithName() {
        return this.recipes;
    }
}

package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AMessage;

public class DeleteRecipeMessage extends AMessage {

    private final long recipesPoolId;
    private final long recipeId;
    
    public DeleteRecipeMessage(long id, long recipesPoolId, long recipeId) {
        super(id);
        this.recipesPoolId = recipesPoolId;
        this.recipeId = recipeId;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }

    public long getRecipeId() {
        return recipeId;
    }
}

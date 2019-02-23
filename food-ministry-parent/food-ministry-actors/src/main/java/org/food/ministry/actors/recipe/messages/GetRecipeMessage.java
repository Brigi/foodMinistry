package org.food.ministry.actors.recipe.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for requesting a recipe.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipeMessage extends AMessage {
    
    /**
     * The id of the recipe
     */
    private final long recipeId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param recipeId The id of the recipe
     */
    public GetRecipeMessage(long id, long recipeId) {
        super(id);
        this.recipeId = recipeId;
    }

    /**
     * Gets the id of the recipe
     * 
     * @return The id of the recipe
     */
    public long getRecipeId() {
        return recipeId;
    }
}

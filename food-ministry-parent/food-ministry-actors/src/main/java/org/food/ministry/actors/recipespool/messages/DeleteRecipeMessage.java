package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for deleting a recipe from a recipes pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class DeleteRecipeMessage extends AMessage {

    /**
     * The id of the recipes pool
     */
    private final long recipesPoolId;
    /**
     * The id of the recipe
     */
    private final long recipeId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param recipesPoolId The id of the recipes pool
     * @param recipeId The id of the recipe
     */
    public DeleteRecipeMessage(long id, long recipesPoolId, long recipeId) {
        super(id);
        this.recipesPoolId = recipesPoolId;
        this.recipeId = recipeId;
    }

    /**
     * Gets the id of the recipes pool
     * 
     * @return The id of the recipes pool
     */
    public long getRecipesPoolId() {
        return recipesPoolId;
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

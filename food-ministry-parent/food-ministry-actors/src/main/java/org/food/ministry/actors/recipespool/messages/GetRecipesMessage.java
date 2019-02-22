package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for requesting the id of a recipes.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipesMessage extends AMessage {

    /**
     * The id of the recipes pool
     */
    private final long recipesPoolId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param recipesPoolId The id of the recipes pool
     */
    public GetRecipesMessage(long id, long recipesPoolId) {
        super(id);
        this.recipesPoolId = recipesPoolId;
    }

    /**
     * Gets the id of the recipes pool
     * 
     * @return The id of the recipes pool
     */
    public long getRecipesPoolId() {
        return recipesPoolId;
    }
}

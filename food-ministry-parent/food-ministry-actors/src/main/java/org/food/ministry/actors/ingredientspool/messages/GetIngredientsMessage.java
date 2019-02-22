package org.food.ministry.actors.ingredientspool.messages;

import org.food.ministry.actors.messages.AMessage;

/**
 * The message for requesting the id of an ingredient.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsMessage extends AMessage {

    /**
     * The id of the ingredients pool
     */
    private final long ingredientsPoolId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param ingredientsPoolId The id of the recipes pool
     */
    public GetIngredientsMessage(long id, long ingredientsPoolId) {
        super(id);
        this.ingredientsPoolId = ingredientsPoolId;
    }

    /**
     * Gets the id of the ingredients pool
     * 
     * @return The id of the ingredients pool
     */
    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }
}

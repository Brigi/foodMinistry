package org.food.ministry.actors.ingredientspool.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for deleting an ingredient from a ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class DeleteIngredientMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the ingredients pool
     */
    private final long ingredientsPoolId;
    /**
     * The id of the ingredient
     */
    private final long ingredientId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param ingredientsPoolId The id of the ingredients pool
     * @param ingredientId The id of the ingredient
     */
    public DeleteIngredientMessage(long id, long ingredientsPoolId, long ingredientId) {
        super(id);
        this.ingredientsPoolId = ingredientsPoolId;
        this.ingredientId = ingredientId;
    }

    /**
     * Gets the id of the ingredients pool
     * 
     * @return The id of the ingredients pool
     */
    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }

    /**
     * Gets the id of the ingredient
     * 
     * @return The id of the ingredient
     */
    public long getIngredientId() {
        return ingredientId;
    }
}

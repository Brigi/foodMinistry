package org.food.ministry.actors.ingredient.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for requesting the id of an ingredient.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientMessage extends AMessage implements IRequestMessage {
    
    /**
     * The id of the ingredient
     */
    private final long ingredientId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param ingredientId The id of the ingredient
     */
    public GetIngredientMessage(long id, long ingredientId) {
        super(id);
        this.ingredientId = ingredientId;
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

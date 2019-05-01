package org.food.ministry.actors.ingredientspool.messages;

import java.util.Set;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of requested ingredients.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsResultMessage extends AResultMessage {

    /**
     * A set containing all ingredient ids
     */
    private final Set<Long> ingredients;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param ingredients A set containing all ingredient ids
     */
    public GetIngredientsResultMessage(long id, long originId, boolean successful, String errorMessage, Set<Long> ingredients) {
        super(id, originId, successful, errorMessage);
        this.ingredients = ingredients;
    }

    /**
     * Gets the set containing all ingredient ids
     * 
     * @return The set containing all ingredient ids
     */
    public Set<Long> getIngredientIds() {
        return this.ingredients;
    }
}

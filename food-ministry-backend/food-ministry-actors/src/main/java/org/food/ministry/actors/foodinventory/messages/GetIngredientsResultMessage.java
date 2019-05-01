package org.food.ministry.actors.foodinventory.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of requested ingredients.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsResultMessage extends AResultMessage {

    /**
     * A map containing all ingredient ids with their corresponding amount
     */
    private final Map<Long, Float> ingredientsWithAmount;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param ingredientsWithAmount A map containing all ingredient ids with their corresponding amount
     */
    public GetIngredientsResultMessage(long id, long originId, boolean successful, String errorMessage, Map<Long, Float> ingredientsWithAmount) {
        super(id, originId, successful, errorMessage);
        this.ingredientsWithAmount = ingredientsWithAmount;
    }

    /**
     * Gets a map containing all ingredient ids with their corresponding amount
     * 
     * @return A map containing all ingredient ids with their corresponding amount
     */
    public Map<Long, Float> getIngredientsWithAmount() {
        return ingredientsWithAmount;
    }
}

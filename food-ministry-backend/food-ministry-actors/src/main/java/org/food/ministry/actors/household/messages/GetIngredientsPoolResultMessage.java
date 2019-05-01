package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of a requested ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsPoolResultMessage extends AResultMessage {

    /**
     * The id of the ingredients pool
     */
    private final long ingredientsPoolId;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param ingredientsPoolId The id of the requested ingredients pool
     */
    public GetIngredientsPoolResultMessage(long id, long originId, boolean successful, String errorMessage, long ingredientsPoolId) {
        super(id, originId, successful, errorMessage);
        this.ingredientsPoolId = ingredientsPoolId;
    }

    /**
     * The id of the requested ingredients pool, if found; else 0.
     * 
     * @return The id of the requested ingredients pool
     */
    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }
}

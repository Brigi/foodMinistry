package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of a requested recipes pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipesPoolResultMessage extends AResultMessage {

    /**
     * The id of the recipes pool
     */
    private final long recipesPoolId;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param recipesPoolId The id of the requested recipes pool
     */
    public GetRecipesPoolResultMessage(long id, long originId, boolean successful, String errorMessage, long recipesPoolId) {
        super(id, originId, successful, errorMessage);
        this.recipesPoolId = recipesPoolId;
    }

    /**
     * The id of the requested recipes pool, if found; else 0.
     * 
     * @return The id of the requested recipes pool
     */
    public long getRecipesPoolId() {
        return recipesPoolId;
    }
}

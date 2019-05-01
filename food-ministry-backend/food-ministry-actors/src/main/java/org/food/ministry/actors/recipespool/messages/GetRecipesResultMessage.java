package org.food.ministry.actors.recipespool.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of requested recipes.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipesResultMessage extends AResultMessage {

    /**
     * A map containing the ids of the recipes and their corresponding name
     */
    private final Map<Long, String> recipes;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param recipes A map containing the ids of the recipes and their
     *            corresponding name
     */
    public GetRecipesResultMessage(long id, long originId, boolean successful, String errorMessage, Map<Long, String> recipes) {
        super(id, originId, successful, errorMessage);
        this.recipes = recipes;
    }

    /**
     * Gets the map containing the ids of the recipes and their corresponding name
     * 
     * @return A map containing the ids of the recipes and their corresponding names
     */
    public Map<Long, String> getRecipesIdsWithName() {
        return this.recipes;
    }
}

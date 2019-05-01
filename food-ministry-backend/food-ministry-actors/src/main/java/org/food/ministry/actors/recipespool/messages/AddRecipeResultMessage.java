package org.food.ministry.actors.recipespool.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of an attempt to add a recipe.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddRecipeResultMessage extends AResultMessage {

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     */
    public AddRecipeResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}

package org.food.ministry.actors.foodinventory.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of an attempt to add/remove an ingredient.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddIngredientResultMessage extends AResultMessage {

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     */
    public AddIngredientResultMessage(long id, long originId, boolean successful, String errorMessage) {
        super(id, originId, successful, errorMessage);
    }
}

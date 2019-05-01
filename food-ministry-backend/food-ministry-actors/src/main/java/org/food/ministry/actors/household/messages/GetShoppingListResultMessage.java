package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of a requested shopping list.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetShoppingListResultMessage extends AResultMessage {

    /**
     * The id of the shopping list
     */
    private final long shoppingListId;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param shoppingListId The id of the requested shopping list
     */
    public GetShoppingListResultMessage(long id, long originId, boolean successful, String errorMessage, long shoppingListId) {
        super(id, originId, successful, errorMessage);
        this.shoppingListId = shoppingListId;
    }

    /**
     * The id of the requested shopping list, if found; else 0.
     * 
     * @return The id of the requested shopping list
     */
    public long getShoppingListId() {
        return shoppingListId;
    }
}

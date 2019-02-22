package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of a requested food inventory
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetFoodInventoryResultMessage extends AResultMessage {

    /**
     * The id of the food inventory
     */
    private final long foodInventoryId;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param foodInventoryId The id of the requested food inventory
     */
    public GetFoodInventoryResultMessage(long id, long originId, boolean successful, String errorMessage, long foodInventoryId) {
        super(id, originId, successful, errorMessage);
        this.foodInventoryId = foodInventoryId;
    }

    /**
     * The id of the requested food inventory, if found; else 0.
     * 
     * @return The id of the requested food inventory
     */
    public long getFoodInventoryId() {
        return foodInventoryId;
    }
}

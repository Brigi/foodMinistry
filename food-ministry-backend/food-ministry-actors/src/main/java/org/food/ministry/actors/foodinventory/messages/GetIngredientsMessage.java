package org.food.ministry.actors.foodinventory.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for requesting the ids and amount of all ingredients inside a food inventory.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the food inventory
     */
    private final long foodInventoryId;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param foodInventoryId The id of the food inventory
     */
    public GetIngredientsMessage(long id, long foodInventoryId) {
        super(id);
        this.foodInventoryId = foodInventoryId;
    }

    /**
     * Gets the id of the food inventory
     * 
     * @return The id of the food inventory
     */
    public long getFoodInventoryId() {
        return foodInventoryId;
    }
}

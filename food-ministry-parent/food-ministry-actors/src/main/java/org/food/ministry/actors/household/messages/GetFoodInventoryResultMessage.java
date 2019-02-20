package org.food.ministry.actors.household.messages;

import org.food.ministry.actors.messages.AResultMessage;

public class GetFoodInventoryResultMessage extends AResultMessage {

    private final long foodInventoryId;
    
    public GetFoodInventoryResultMessage(long id, long originId, boolean successful, String errorMessage, long foodInventoryId) {
        super(id, originId, successful, errorMessage);
        this.foodInventoryId = foodInventoryId;
    }

    public long getFoodInventoryId() {
        return foodInventoryId;
    }
}

package org.food.ministry.rest.foodinventory.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class BaseFoodInventoryRequestJSON extends BaseRequestJSON {

    private long foodInventoryId;
    
    public BaseFoodInventoryRequestJSON() { }
    
    public BaseFoodInventoryRequestJSON(long userId, long foodInventoryId) {
        super(userId);
        this.foodInventoryId = foodInventoryId;
    }

    public long getFoodInventoryId() {
        return foodInventoryId;
    }

    public void setFoodInventoryId(long foodInventoryId) {
        this.foodInventoryId = foodInventoryId;
    }
}

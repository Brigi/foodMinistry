package org.food.ministry.rest.household.json;

public class GetFoodInventoryResultJSON {

    private long foodInventoryId;
    
    public GetFoodInventoryResultJSON() { }
    
    public GetFoodInventoryResultJSON(long foodInventoryId) {
        this.foodInventoryId = foodInventoryId;
    }

    public long getFoodInventoryId() {
        return foodInventoryId;
    }

    public void setFoodInventoryId(long foodInventoryId) {
        this.foodInventoryId = foodInventoryId;
    }
}

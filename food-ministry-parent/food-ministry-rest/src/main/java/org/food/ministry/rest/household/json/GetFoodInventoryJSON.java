package org.food.ministry.rest.household.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetFoodInventoryJSON extends BaseRequestJSON {
    
    private long householdId;
    
    public GetFoodInventoryJSON() { }
    
    public GetFoodInventoryJSON(long userId, long householdId) {
        super(userId);
        this.householdId = householdId;
    }

    public long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(long householdId) {
        this.householdId = householdId;
    }
}

package org.food.ministry.rest.household.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetIngredientsPoolJSON extends BaseRequestJSON {
    
    private long householdId;
    
    public GetIngredientsPoolJSON() { }
    
    public GetIngredientsPoolJSON(long userId, long householdId) {
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

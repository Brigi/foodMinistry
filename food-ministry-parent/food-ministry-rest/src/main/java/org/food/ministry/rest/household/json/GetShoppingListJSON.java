package org.food.ministry.rest.household.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetShoppingListJSON extends BaseRequestJSON {
    
    private long householdId;
    
    public GetShoppingListJSON() { }
    
    public GetShoppingListJSON(long userId, long householdId) {
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

package org.food.ministry.rest.household.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class BaseHouseholdRequestJSON extends BaseRequestJSON {
    
    private long householdId;
    
    public BaseHouseholdRequestJSON() { }
    
    public BaseHouseholdRequestJSON(long userId, long householdId) {
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

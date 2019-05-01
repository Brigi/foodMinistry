package org.food.ministry.rest.user.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class RemoveHouseholdJSON extends BaseRequestJSON {
    
    private long householdId;
    
    public RemoveHouseholdJSON() {  }
    
    public RemoveHouseholdJSON(long userId, long householdId) {
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

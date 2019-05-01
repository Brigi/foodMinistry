package org.food.ministry.rest.user.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class AddHouseholdJSON extends BaseRequestJSON {
    
    private String householdName;
    
    public AddHouseholdJSON() {  }
    
    public AddHouseholdJSON(long userId, String householdName) {
        super(userId);
        this.householdName = householdName;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }
}

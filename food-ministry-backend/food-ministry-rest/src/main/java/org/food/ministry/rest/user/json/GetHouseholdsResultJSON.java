package org.food.ministry.rest.user.json;

import java.util.Map;

public class GetHouseholdsResultJSON {

    private Map<Long, String> households;
    
    public GetHouseholdsResultJSON() { }
    
    public GetHouseholdsResultJSON(Map<Long, String> households) {
        this.households = households;
    }

    public Map<Long, String> getHouseholds() {
        return households;
    }

    public void setHouseholds(Map<Long, String> households) {
        this.households = households;
    }
}

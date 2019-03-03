package org.food.ministry.rest.user.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetHouseholdsJSON extends BaseRequestJSON {
    
    public GetHouseholdsJSON() { }
    
    public GetHouseholdsJSON(long userId) {
        super(userId);
    }
}

package org.food.ministry.rest.ingredientspool.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class BaseIngredientsPoolRequestJSON extends BaseRequestJSON {

    private long ingredientsPoolId;
    
    public BaseIngredientsPoolRequestJSON() { }
    
    public BaseIngredientsPoolRequestJSON(long userId, long ingredientsPoolId) {
        super(userId);
        this.ingredientsPoolId = ingredientsPoolId;
    }

    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }

    public void setIngredientsPoolId(long ingredientsPoolId) {
        this.ingredientsPoolId = ingredientsPoolId;
    }
}

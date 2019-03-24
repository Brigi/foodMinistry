package org.food.ministry.rest.recipespool.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class BaseRecipesPoolRequestJSON extends BaseRequestJSON {

    private long recipesPoolId;
    
    public BaseRecipesPoolRequestJSON() { }
    
    public BaseRecipesPoolRequestJSON(long userId, long recipesPoolId) {
        super(userId);
        this.recipesPoolId = recipesPoolId;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }

    public void setRecipesPoolId(long recipesPoolId) {
        this.recipesPoolId = recipesPoolId;
    }
}

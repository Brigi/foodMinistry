package org.food.ministry.rest.household.json;

public class GetRecipesPoolResultJSON {

    private long recipesPoolId;
    
    public GetRecipesPoolResultJSON() { }
    
    public GetRecipesPoolResultJSON(long recipesPoolId) {
        this.recipesPoolId = recipesPoolId;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }

    public void setRecipesPoolId(long recipesPoolId) {
        this.recipesPoolId = recipesPoolId;
    }
}

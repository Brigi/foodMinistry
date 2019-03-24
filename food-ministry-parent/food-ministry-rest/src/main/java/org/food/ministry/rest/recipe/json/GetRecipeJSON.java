package org.food.ministry.rest.recipe.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetRecipeJSON extends BaseRequestJSON {
    
    private long recipeId;
    
    public GetRecipeJSON() { }
    
    public GetRecipeJSON(long userId, long recipeId) {
        super(userId);
        this.recipeId = recipeId;
    }
    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}

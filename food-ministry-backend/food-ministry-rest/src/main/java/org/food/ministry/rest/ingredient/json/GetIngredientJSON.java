package org.food.ministry.rest.ingredient.json;

import org.food.ministry.rest.json.BaseRequestJSON;

public class GetIngredientJSON extends BaseRequestJSON {
    
    private long ingredientId;
    
    public GetIngredientJSON() { }
    
    public GetIngredientJSON(long userId, long ingredientId) {
        super(userId);
        this.ingredientId = ingredientId;
    }
    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }
}

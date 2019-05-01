package org.food.ministry.rest.ingredientspool.json;

import java.util.Set;

public class GetIngredientsResultJSON {

    private Set<Long> ingredientIds;
    
    public GetIngredientsResultJSON() { }
    
    public GetIngredientsResultJSON(Set<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public Set<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(Set<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}

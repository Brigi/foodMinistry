package org.food.ministry.rest.foodinventory.json;

import java.util.Map;

public class GetIngredientsResultJSON {

    private Map<Long, Float> ingredientsWithAmount;
    
    public GetIngredientsResultJSON() { }
    
    public GetIngredientsResultJSON(Map<Long, Float> ingredientsWithAmount) {
        this.ingredientsWithAmount = ingredientsWithAmount;
    }

    public Map<Long, Float> getIngredientsWithAmount() {
        return ingredientsWithAmount;
    }

    public void setIngredientsWithAmount(Map<Long, Float> ingredientsWithAmount) {
        this.ingredientsWithAmount = ingredientsWithAmount;
    }
}

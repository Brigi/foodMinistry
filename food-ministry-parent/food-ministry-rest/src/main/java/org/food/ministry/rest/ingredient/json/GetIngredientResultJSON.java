package org.food.ministry.rest.ingredient.json;

import org.food.ministry.model.Unit;

public class GetIngredientResultJSON {

    private String ingredientName;
    
    private Unit ingredientUnit;
    
    private boolean isIngredientBasic;
    
    public GetIngredientResultJSON() { }
    
    public GetIngredientResultJSON(String ingredientName, Unit ingredientUnit, boolean isIngredientBasic) {
        this.ingredientName = ingredientName;
        this.ingredientUnit = ingredientUnit;
        this.isIngredientBasic = isIngredientBasic;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Unit getIngredientUnit() {
        return ingredientUnit;
    }

    public void setIngredientUnit(Unit ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    public boolean isIngredientBasic() {
        return isIngredientBasic;
    }

    public void setIngredientBasic(boolean isIngredientBasic) {
        this.isIngredientBasic = isIngredientBasic;
    }
}

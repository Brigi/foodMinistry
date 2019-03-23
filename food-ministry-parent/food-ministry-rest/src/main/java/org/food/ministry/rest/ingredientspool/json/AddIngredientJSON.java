package org.food.ministry.rest.ingredientspool.json;

import org.food.ministry.model.Unit;

public class AddIngredientJSON extends BaseIngredientsPoolRequestJSON {

    /**
     * The name of the ingredient
     */
    private String ingredientName;
    /**
     * The unit of the ingredient
     */
    private Unit unit;
    /**
     * Determines if the ingredient is a basic ingredient
     */
    private boolean isBasic;
    
    public AddIngredientJSON() { }
    
    public AddIngredientJSON(long userId, long ingredientsPoolId, String ingredientName, Unit unit, boolean isBasic) {
        super(userId, ingredientsPoolId);
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.isBasic = isBasic;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isBasic() {
        return isBasic;
    }

    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }
}

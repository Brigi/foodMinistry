package org.food.ministry.rest.recipe.json;

import java.util.Map;

public class GetRecipeResultJSON {

    private String recipeName;
    
    private String recipeDescription;
    
    private Map<Long, Float> ingredientWithAmount;
    
    public GetRecipeResultJSON() { }
    
    public GetRecipeResultJSON(String recipeName, String recipeDescription, Map<Long, Float> ingredientWithAmount) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.ingredientWithAmount = ingredientWithAmount;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public Map<Long, Float> getIngredientWithAmount() {
        return ingredientWithAmount;
    }

    public void setIngredientWithAmount(Map<Long, Float> ingredientWithAmount) {
        this.ingredientWithAmount = ingredientWithAmount;
    }
}

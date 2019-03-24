package org.food.ministry.rest.recipespool.json;

import java.util.Map;

public class AddRecipeJSON extends BaseRecipesPoolRequestJSON {

    private String recipeName;
    
    private String recipeDescription;

    private Map<Long, Float> ingredientsWithAmount;
    
    public AddRecipeJSON() { }
    
    public AddRecipeJSON(long userId, long recipesPoolId, String recipeName, Map<Long, Float> ingredientsWithAmount, String recipeDescription) {
        super(userId, recipesPoolId);
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.ingredientsWithAmount = ingredientsWithAmount;
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

    public Map<Long, Float> getIngredientsWithAmount() {
        return ingredientsWithAmount;
    }

    public void setIngredientsWithAmount(Map<Long, Float> ingredientsWithAmount) {
        this.ingredientsWithAmount = ingredientsWithAmount;
    }
}

package org.food.ministry.actors.recipespool.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AMessage;

public class AddRecipeMessage extends AMessage {

    private final long recipesPoolId;
    private final String recipeName;
    private final Map<Long, Float> ingredientIdToAmountMap;
    private final String recipeDescription;
    
    public AddRecipeMessage(long id, long recipesPoolId, String recipeName, Map<Long, Float> ingredientIdToAmountMap, String recipeDescription) {
        super(id);
        this.recipesPoolId = recipesPoolId;
        this.recipeName = recipeName;
        this.ingredientIdToAmountMap = ingredientIdToAmountMap;
        this.recipeDescription = recipeDescription;
    }

    public long getRecipesPoolId() {
        return recipesPoolId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Map<Long, Float> getIngredientIdsWithAmount() {
        return ingredientIdToAmountMap;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }
}

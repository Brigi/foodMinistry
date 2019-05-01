package org.food.ministry.actors.recipespool.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for adding a recipe to a recipes pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddRecipeMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the recipes pool
     */
    private final long recipesPoolId;
    /**
     * The name of the recipe
     */
    private final String recipeName;
    /**
     * A map containing the ids of the needed ingredients with their amount
     */
    private final Map<Long, Float> ingredientIdToAmountMap;
    /**
     * The description of the recipe
     */
    private final String recipeDescription;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param recipesPoolId The id of the recipes pool
     * @param recipeName The name of the recipe
     * @param ingredientIdToAmountMap A map containing the ids of the needed
     *            ingredients with their amount
     * @param recipeDescription The description of the recipe
     */
    public AddRecipeMessage(long id, long recipesPoolId, String recipeName, Map<Long, Float> ingredientIdToAmountMap, String recipeDescription) {
        super(id);
        this.recipesPoolId = recipesPoolId;
        this.recipeName = recipeName;
        this.ingredientIdToAmountMap = ingredientIdToAmountMap;
        this.recipeDescription = recipeDescription;
    }

    /**
     * Gets the id of the recipes pool
     * 
     * @return The id of the recipes pool
     */
    public long getRecipesPoolId() {
        return recipesPoolId;
    }

    /**
     * Gets the name of the recipe
     * 
     * @return The name of the recipe
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Gets mapping between ingredient ids and their amount
     * 
     * @return The mapping between ingredient ids and their amount
     */
    public Map<Long, Float> getIngredientIdsWithAmount() {
        return ingredientIdToAmountMap;
    }

    /**
     * Gets the description of the recipe
     * 
     * @return The description of the recipe
     */
    public String getRecipeDescription() {
        return recipeDescription;
    }
}

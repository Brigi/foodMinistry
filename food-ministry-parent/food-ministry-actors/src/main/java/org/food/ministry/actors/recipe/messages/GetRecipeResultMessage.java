package org.food.ministry.actors.recipe.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of requested recipes.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipeResultMessage extends AResultMessage {

    /**
     * The name of the recipe
     */
    private final String recipeName;

    /**
     * A map containing the ingredients for the recipe with the amount needed
     */
    private final Map<Long, Float> ingredientsWithAmount;
    
    /**
     * The description of the recipe
     */
    private final String recipeDescription;
    
    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param recipeName The name of the recipe
     * @param ingredientsWithAmount A map containing the ingredients for the recipe with the amount needed
     * @param recipeDescription The description of the recipe
     */
    public GetRecipeResultMessage(long id, long originId, boolean successful, String errorMessage, String recipeName, Map<Long, Float> ingredientsWithAmount, String recipeDescription) {
        super(id, originId, successful, errorMessage);
        this.recipeName = recipeName;
        this.ingredientsWithAmount = ingredientsWithAmount;
        this.recipeDescription = recipeDescription;
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
     * Gets a map containing the ingredients for the recipe with the amount needed
     * 
     * @return A map containing the ingredients for the recipe with the amount needed
     */
    public Map<Long, Float> getIngredientsWithAmount() {
        return ingredientsWithAmount;
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

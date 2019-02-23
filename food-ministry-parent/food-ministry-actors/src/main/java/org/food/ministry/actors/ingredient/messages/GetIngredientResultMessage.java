package org.food.ministry.actors.ingredient.messages;

import org.food.ministry.actors.messages.AResultMessage;
import org.food.ministry.model.Unit;

/**
 * The message for transmitting the result of requested ingredients.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientResultMessage extends AResultMessage {

    /**
     * The name of the ingredient
     */
    private final String ingredientName;

    /**
     * The unit of the ingredient
     */
    private final Unit ingredientUnit;
    
    /**
     * Determines if the ingredient is a basic ingredient
     */
    private final boolean isBasic;
    
    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param ingredientName The name of the ingredient
     * @param ingredientUnit The unit of the ingredient
     * @param isBasic Determines if the ingredient is a basic ingredient
     */
    public GetIngredientResultMessage(long id, long originId, boolean successful, String errorMessage, String ingredientName, Unit ingredientUnit, boolean isBasic) {
        super(id, originId, successful, errorMessage);
        this.ingredientName = ingredientName;
        this.ingredientUnit = ingredientUnit;
        this.isBasic = isBasic;
    }

    /**
     * Gets the name of the ingredient
     * 
     * @return The name of the ingredient
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * Gets the unit of the ingredient
     * 
     * @return The unit of the ingredient
     */
    public Unit getIngredientUnit() {
        return ingredientUnit;
    }

    /**
     * Determines if the ingredient is a basic ingredient
     * 
     * @return True, if it is a basic ingredient; else false
     */
    public boolean isBasicIngredient() {
        return isBasic;
    }
}

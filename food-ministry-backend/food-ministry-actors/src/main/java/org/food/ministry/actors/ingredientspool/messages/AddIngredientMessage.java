package org.food.ministry.actors.ingredientspool.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;
import org.food.ministry.model.Unit;

/**
 * The message for adding a ingredient to a ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddIngredientMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the ingredients pool
     */
    private final long ingredientsPoolId;
    /**
     * The name of the ingredient
     */
    private final String ingredientName;
    /**
     * The unit of the ingredient
     */
    private final Unit unit;
    /**
     * Determines if the ingredient is a basic ingredient
     */
    private final boolean isBasic;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param ingredientsPoolId The id of the ingredients pool
     * @param ingredientName The name of the ingredient
     * @param unit The unit of the ingredient
     * @param isBasic Determines if the ingredient is a basic ingredient
     */
    public AddIngredientMessage(long id, long ingredientsPoolId, String ingredientName, Unit unit, boolean isBasic) {
        super(id);
        this.ingredientsPoolId = ingredientsPoolId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.isBasic = isBasic;
    }

    /**
     * Gets the id of the ingredients pool
     * 
     * @return The id of the ingredients pool
     */
    public long getIngredientsPoolId() {
        return ingredientsPoolId;
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
    public Unit getUnit() {
        return unit;
    }

    /**
     * Determines if the ingredient is a basic ingredient
     * 
     * @return True, if it is a basic ingredient; else false
     */
    public boolean isBasic() {
        return isBasic;
    }
}

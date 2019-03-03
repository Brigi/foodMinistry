package org.food.ministry.actors.foodinventory.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for adding/removing an ingredient to a food inventory.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddIngredientMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the food inventory
     */
    private final long foodInventoryId;
    /**
     * The id of the ingredient
     */
    private final long ingredientId;
    /**
     * The amount of the ingredient
     */
    private final float ingredientAmount;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param foodInventoryId The id of the food inventory
     * @param ingredientId The id of the ingredient
     * @param ingredientAmount The amount of the ingredient
     */
    public AddIngredientMessage(long id, long foodInventoryId, long ingredientId, float ingredientAmount) {
        super(id);
        this.foodInventoryId = foodInventoryId;
        this.ingredientId = ingredientId;
        this.ingredientAmount = ingredientAmount;
    }

    /**
     * Gets the id of the food inventory
     * 
     * @return The id of the food inventory
     */
    public long getFoodInventoryId() {
        return foodInventoryId;
    }

    /**
     * Gets the id of the ingredient
     * 
     * @return The id of the ingredient
     */
    public long getIngredientId() {
        return ingredientId;
    }

    /**
     * Gets the amount of the ingredient
     * 
     * @return The amount of the ingredient
     */
    public float getIngredientAmount() {
        return ingredientAmount;
    }
}

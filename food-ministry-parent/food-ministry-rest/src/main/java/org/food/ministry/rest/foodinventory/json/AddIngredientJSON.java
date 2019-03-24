package org.food.ministry.rest.foodinventory.json;

public class AddIngredientJSON extends BaseFoodInventoryRequestJSON {

    private long ingredientId;
    
    private float amount;
    
    public AddIngredientJSON() { }
    
    public AddIngredientJSON(long userId, long foodInventoryId, long ingredientId, float amount) {
        super(userId, foodInventoryId);
        this.ingredientId = ingredientId;
        this.amount = amount;
    }

    public long getIngredient() {
        return ingredientId;
    }

    public void setIngredient(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}

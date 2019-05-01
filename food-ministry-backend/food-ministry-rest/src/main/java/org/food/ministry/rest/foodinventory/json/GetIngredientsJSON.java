package org.food.ministry.rest.foodinventory.json;

public class GetIngredientsJSON extends BaseFoodInventoryRequestJSON {
    
    public GetIngredientsJSON() { }
    
    public GetIngredientsJSON(long userId, long householdId) {
        super(userId, householdId);
    }
}

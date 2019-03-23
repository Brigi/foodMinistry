package org.food.ministry.rest.household.json;

public class GetFoodInventoryJSON extends BaseHouseholdRequestJSON {
    
    public GetFoodInventoryJSON() { }
    
    public GetFoodInventoryJSON(long userId, long householdId) {
        super(userId, householdId);
    }
}

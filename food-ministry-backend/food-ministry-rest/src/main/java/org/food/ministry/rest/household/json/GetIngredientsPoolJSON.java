package org.food.ministry.rest.household.json;

public class GetIngredientsPoolJSON extends BaseHouseholdRequestJSON {
    
    public GetIngredientsPoolJSON() { }
    
    public GetIngredientsPoolJSON(long userId, long householdId) {
        super(userId, householdId);
    }
}

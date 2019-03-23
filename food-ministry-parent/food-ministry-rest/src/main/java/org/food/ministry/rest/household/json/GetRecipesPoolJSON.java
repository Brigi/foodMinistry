package org.food.ministry.rest.household.json;

public class GetRecipesPoolJSON extends BaseHouseholdRequestJSON {
    
    public GetRecipesPoolJSON() { }
    
    public GetRecipesPoolJSON(long userId, long householdId) {
        super(userId, householdId);
    }
}

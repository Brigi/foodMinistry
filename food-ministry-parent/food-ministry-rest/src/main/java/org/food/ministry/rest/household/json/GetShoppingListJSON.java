package org.food.ministry.rest.household.json;

public class GetShoppingListJSON extends BaseHouseholdRequestJSON {
    
    public GetShoppingListJSON() { }
    
    public GetShoppingListJSON(long userId, long householdId) {
        super(userId, householdId);
    }
}

package org.food.ministry.rest.household.json;

public class GetShoppingListResultJSON {

    private long shoppingListId;
    
    public GetShoppingListResultJSON() { }
    
    public GetShoppingListResultJSON(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}

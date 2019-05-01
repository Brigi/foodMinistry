package org.food.ministry.rest.household.json;

public class GetIngredientsPoolResultJSON {

    private long ingredientsPoolId;
    
    public GetIngredientsPoolResultJSON() { }
    
    public GetIngredientsPoolResultJSON(long ingredientsPoolId) {
        this.ingredientsPoolId = ingredientsPoolId;
    }

    public long getIngredientsPoolId() {
        return ingredientsPoolId;
    }

    public void setIngredientsPoolId(long ingredientsPoolId) {
        this.ingredientsPoolId = ingredientsPoolId;
    }
}

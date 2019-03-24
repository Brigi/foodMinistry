package org.food.ministry.rest.ingredientspool.json;

public class GetIngredientsJSON extends BaseIngredientsPoolRequestJSON {
    
    public GetIngredientsJSON() { }
    
    public GetIngredientsJSON(long userId, long ingredientsPoolId) {
        super(userId, ingredientsPoolId);
    }
}

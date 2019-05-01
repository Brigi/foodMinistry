package org.food.ministry.rest.recipespool.json;

public class GetRecipesJSON extends BaseRecipesPoolRequestJSON {
    
    public GetRecipesJSON() { }
    
    public GetRecipesJSON(long userId, long recipesPoolId) {
        super(userId, recipesPoolId);
    }
}

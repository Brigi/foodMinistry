package org.food.ministry.rest.recipespool.json;

import java.util.Map;

public class GetRecipesResultJSON {

    private Map<Long, String> recipesWithName;
    
    public GetRecipesResultJSON() { }
    
    public GetRecipesResultJSON(Map<Long, String> recipesWithName) {
        this.recipesWithName = recipesWithName;
    }

    public Map<Long, String> getRecipesWithName() {
        return recipesWithName;
    }

    public void setRecipesWithName(Map<Long, String> recipesWithName) {
        this.recipesWithName = recipesWithName;
    }
}

package org.food.ministry.model;

import java.util.Map;

public class Recipe {

    private String name;
    
    private Map<Ingredient, Float> ingredients;
    
    private String description;
    
    public Recipe(String name, Map<Ingredient, Float> ingredients, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Ingredient, Float> getIngredientsWithQuantity() {
        return ingredients;
    }

    public void setIngredientsWithQuantity(Map<Ingredient, Float> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

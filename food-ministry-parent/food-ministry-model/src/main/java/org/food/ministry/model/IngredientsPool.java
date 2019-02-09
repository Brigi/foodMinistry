package org.food.ministry.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.food.ministry.model.exception.IngredientNotFoundException;

public class IngredientsPool {

    private Set<Ingredient> ingredients;
    
    public IngredientsPool() {
        this.ingredients = new HashSet<Ingredient>();
    }
    
    public Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(this.ingredients);
    }
    
    public Ingredient getIngredient(String name) throws IngredientNotFoundException {
        if(name == null) {
            throw new IllegalArgumentException("Can't search for ingredient given as 'null'");
        }
        for(Ingredient ingredient: ingredients) {
            if(name.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        
        throw new IngredientNotFoundException("The ingredient '" + name + "' was not found!");
    }
    
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }
}

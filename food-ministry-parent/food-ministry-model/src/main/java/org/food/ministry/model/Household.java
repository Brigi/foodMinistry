package org.food.ministry.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Household {

    private String name;
    
    private FoodInventory foodInventory;
    
    private ShoppingList shoppingList;
    
    private IngredientsPool ingredientsPool;
    
    private Set<Recipe> recipes;
    
    public Household(String name) {
        this.name = name;
        this.foodInventory = new FoodInventory();
        this.shoppingList = new ShoppingList();
        this.ingredientsPool = new IngredientsPool();
        this.recipes = new HashSet<>();
    }

    public IngredientsPool getIngredientsPool() {
        return ingredientsPool;
    }

    public void setIngredientsPool(IngredientsPool ingredientsPool) {
        this.ingredientsPool = ingredientsPool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Recipe> getRecipes() {
        return Collections.unmodifiableSet(recipes);
    }

    public void addRecipes(Set<Recipe> recipes) {
        this.recipes.addAll(recipes);
    }
    
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }
    
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }

    public FoodInventory getFoodInventory() {
        return foodInventory;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }
}

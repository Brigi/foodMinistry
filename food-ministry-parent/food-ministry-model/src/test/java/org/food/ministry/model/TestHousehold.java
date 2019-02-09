package org.food.ministry.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TestHousehold {

    @Test
    public void testGetName() {
        final String name = "My Household";
        Household household = new Household(name);
        Assert.assertEquals(name, household.getName());
    }
    
    @Test
    public void testSetName() {
        final String name = "My Household";
        Household household = new Household("Something different");
        household.setName(name);
        Assert.assertEquals(name, household.getName());
    }
    
    @Test
    public void testFoodInventoryNotNull() {
        final String name = "My Household";
        Household household = new Household(name);
        Assert.assertNotNull(name, household.getFoodInventory());
    }
    
    @Test
    public void testIngredientsPoolNotNull() {
        final String name = "My Household";
        Household household = new Household(name);
        Assert.assertNotNull(name, household.getIngredientsPool());
    }
    
    @Test
    public void testSetIngredientsPool() {
        final String name = "My Household";
        Household household = new Household(name);
        IngredientsPool defaultPool = household.getIngredientsPool();
        household.setIngredientsPool(new IngredientsPool());
        Assert.assertNotEquals(defaultPool, household.getIngredientsPool());
    }
    
    @Test
    public void testShoppingListNotNull() {
        final String name = "My Household";
        Household household = new Household(name);
        Assert.assertNotNull(name, household.getShoppingList());
    }
    
    @Test
    public void testAddRecipe() {
        final String name = "My Household";
        Household household = new Household(name);
        Recipe recipe = new Recipe("Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        household.addRecipe(recipe);
        Assert.assertEquals(1, household.getRecipes().size());
    }
    
    @Test
    public void testAddRecipes() {
        final String name = "My Household";
        Household household = new Household(name);
        Recipe recipe1 = new Recipe("Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        Recipe recipe2 = new Recipe("Schnitzel with French Fries", new HashMap<Ingredient, Float>(), "Yummy!");
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        household.addRecipes(recipes);
        Assert.assertEquals(2, household.getRecipes().size());
    }
    
    @Test
    public void testRemoveRecipe() {
        final String name = "My Household";
        Household household = new Household(name);
        Recipe recipe = new Recipe("Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        household.addRecipe(recipe);
        household.removeRecipe(recipe);
        Assert.assertEquals(0, household.getRecipes().size());
    }
}

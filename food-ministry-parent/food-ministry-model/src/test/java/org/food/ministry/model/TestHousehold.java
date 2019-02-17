package org.food.ministry.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestHousehold {

    private static final String HOUSEHOLD_NAME = "My Household";

    private Household household;

    @Before
    public void startUp() {
        FoodInventory foodInventory = new FoodInventory(0);
        ShoppingList shoppingList = new ShoppingList(0);
        IngredientsPool ingredientsPool = new IngredientsPool(0);
        household = new Household(0, foodInventory, shoppingList, ingredientsPool, HOUSEHOLD_NAME);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(HOUSEHOLD_NAME, household.getName());
    }

    @Test
    public void testSetName() {
        FoodInventory foodInventory = new FoodInventory(0);
        ShoppingList shoppingList = new ShoppingList(0);
        IngredientsPool ingredientsPool = new IngredientsPool(0);
        Household household = new Household(0, foodInventory, shoppingList, ingredientsPool, "Something different");
        household.setName(HOUSEHOLD_NAME);
        Assert.assertEquals(HOUSEHOLD_NAME, household.getName());
    }

    @Test
    public void testFoodInventoryNotNull() {
        Assert.assertNotNull(HOUSEHOLD_NAME, household.getFoodInventory());
    }

    @Test
    public void testIngredientsPoolNotNull() {
        Assert.assertNotNull(HOUSEHOLD_NAME, household.getIngredientsPool());
    }

    @Test
    public void testSetIngredientsPool() {
        IngredientsPool defaultPool = household.getIngredientsPool();
        household.setIngredientsPool(new IngredientsPool(1));
        Assert.assertNotEquals(defaultPool, household.getIngredientsPool());
    }

    @Test
    public void testShoppingListNotNull() {
        Assert.assertNotNull(household.getShoppingList());
    }

    @Test
    public void testAddRecipe() {
        Recipe recipe = new Recipe(0, "Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        household.addRecipe(recipe);
        Assert.assertEquals(1, household.getRecipes().size());
    }

    @Test
    public void testAddRecipes() {
        Recipe recipe1 = new Recipe(0, "Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        Recipe recipe2 = new Recipe(1, "Schnitzel with French Fries", new HashMap<Ingredient, Float>(), "Yummy!");
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        household.addRecipes(recipes);
        Assert.assertEquals(2, household.getRecipes().size());
    }

    @Test
    public void testRemoveRecipe() {
        Recipe recipe = new Recipe(0, "Al forno", new HashMap<Ingredient, Float>(), "Tasty!");
        household.addRecipe(recipe);
        household.removeRecipe(recipe);
        Assert.assertEquals(0, household.getRecipes().size());
    }
}

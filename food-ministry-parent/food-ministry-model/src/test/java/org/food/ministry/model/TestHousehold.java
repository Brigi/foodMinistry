package org.food.ministry.model;

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
        RecipesPool recipesPool = new RecipesPool(0);
        household = new Household(0, foodInventory, shoppingList, ingredientsPool, recipesPool, HOUSEHOLD_NAME);
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(0, household.getId());
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
        RecipesPool recipesPool = new RecipesPool(0);
        Household household = new Household(0, foodInventory, shoppingList, ingredientsPool, recipesPool, "Something different");
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
    public void testRecipePoolNotNull() {
        Assert.assertNotNull(HOUSEHOLD_NAME, household.getRecipesPool());
    }

    @Test
    public void testSetRecipesPool() {
        RecipesPool defaultPool = household.getRecipesPool();
        household.setRecipesPool(new RecipesPool(1));
        Assert.assertNotEquals(defaultPool, household.getRecipesPool());
    }

    @Test
    public void testShoppingListNotNull() {
        Assert.assertNotNull(household.getShoppingList());
    }
}

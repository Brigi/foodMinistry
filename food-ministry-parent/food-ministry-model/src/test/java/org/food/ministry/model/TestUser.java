package org.food.ministry.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestUser {

    private static String NAME = "My Name";
    private static String EMAIL_ADDRESS = "name@provider.com";
    private static String PASSWORD = "password";

    private User user;

    @Before
    public void startUp() {
        user = new User(0, EMAIL_ADDRESS, NAME, PASSWORD);
    }
    
    @Test
    public void testGetId() {
        Assert.assertEquals(0, user.getId());
    }

    @Test
    public void testGetNameAndEmailAddressAndPassword() {
        Assert.assertEquals(NAME, user.getName());
        Assert.assertEquals(EMAIL_ADDRESS, user.getEmailAddress());
        Assert.assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testSetNameAndEmailAddressAndPassword() {
        final String name = "My Other Name";
        final String email = "name@otherProvider.com";
        final String password = "otherPassword";
        user.setName(name);
        user.setEmailAddress(email);
        user.setPassword(password);
        Assert.assertEquals(name, user.getName());
        Assert.assertEquals(email, user.getEmailAddress());
        Assert.assertEquals(password, user.getPassword());
    }

    @Test
    public void testAddHousehold() {
        FoodInventory foodInventory = new FoodInventory(0);
        ShoppingList shoppingList = new ShoppingList(0);
        IngredientsPool ingredientsPool = new IngredientsPool(0);
        RecipesPool recipesPool = new RecipesPool(0);
        Household household = new Household(0, foodInventory, shoppingList, ingredientsPool, recipesPool, "My Household");
        user.addHousehold(household);
        Assert.assertEquals(1, user.getHouseholds().size());
    }

    @Test
    public void testRemoveHousehold() {
        FoodInventory foodInventory = new FoodInventory(0);
        ShoppingList shoppingList = new ShoppingList(0);
        IngredientsPool ingredientsPool = new IngredientsPool(0);
        RecipesPool recipesPool = new RecipesPool(0);
        Household household = new Household(0, foodInventory, shoppingList, ingredientsPool, recipesPool, "My Household");
        user.addHousehold(household);
        user.removeHousehold(household);
        Assert.assertEquals(0, user.getHouseholds().size());
    }
}

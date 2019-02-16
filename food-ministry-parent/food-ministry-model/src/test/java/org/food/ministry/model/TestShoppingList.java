package org.food.ministry.model;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestShoppingList {

    private ShoppingList shoppingList;
    
    @Before
    public void startUp() {
        shoppingList = new ShoppingList(0);
    }
    
    @Test
    public void testInitialIngredients() {
        Assert.assertTrue(new ShoppingList(0).getIngredientsWithQuantity().isEmpty());
    }
    
    @Test
    public void testAddIngredients() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 2);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(2.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testSubstractIngredients() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(zucchini, -2);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(1.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testRemoveIngredients() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(zucchini, -3);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(0, ingredientList.size());
    }
    
    @Test
    public void testToString() {
        final String expectedString = "Noodles: 2 kg\r\nTomato: 6 \r\nWheat: 5 kg\r\nZucchini: 3 \r\n";
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Ingredient noodles = new Ingredient(1, "Noodles", Unit.KILOGRAMM, false);
        Ingredient tomtato = new Ingredient(2, "Tomato", Unit.NONE, false);
        Ingredient wheat = new Ingredient(3, "Wheat", Unit.KILOGRAMM, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(noodles, 2);
        shoppingList.addIngredient(tomtato, 6);
        shoppingList.addIngredient(wheat, 5);
        Assert.assertEquals(expectedString, shoppingList.toString());
    }
}

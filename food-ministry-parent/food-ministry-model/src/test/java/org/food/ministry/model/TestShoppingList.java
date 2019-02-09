package org.food.ministry.model;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class TestShoppingList {

    @Test
    public void testInitialIngredients() {
        Assert.assertTrue(new ShoppingList().getIngredientsWithQuantity().isEmpty());
    }
    
    @Test
    public void testAddIngredients() {
        ShoppingList shoppingList = new ShoppingList();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 2);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(2.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testSubstractIngredients() {
        ShoppingList shoppingList = new ShoppingList();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(zucchini, -2);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(1.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testRemoveIngredients() {
        ShoppingList shoppingList = new ShoppingList();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(zucchini, -3);
        
        Map<Ingredient, Float> ingredientList = shoppingList.getIngredientsWithQuantity();
        Assert.assertEquals(0, ingredientList.size());
    }
    
    @Test
    public void testToString() {
        final String expectedString = "Noodles: 2 kg\r\nTomato: 6 \r\nWheat: 5 kg\r\nZucchini: 3 \r\n";
        ShoppingList shoppingList = new ShoppingList();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        Ingredient noodles = new Ingredient("Noodles", Unit.KILOGRAMM, false);
        Ingredient tomtato = new Ingredient("Tomato", Unit.NONE, false);
        Ingredient wheat = new Ingredient("Wheat", Unit.KILOGRAMM, false);
        shoppingList.addIngredient(zucchini, 3);
        shoppingList.addIngredient(noodles, 2);
        shoppingList.addIngredient(tomtato, 6);
        shoppingList.addIngredient(wheat, 5);
        Assert.assertEquals(expectedString, shoppingList.toString());
    }
}

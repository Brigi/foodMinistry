package org.food.ministry.model;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class TestFoodInventory {


    @Test
    public void testInitialIngredients() {
        Assert.assertTrue(new FoodInventory().getIngredientsWithQuantity().isEmpty());
    }
    
    @Test
    public void testAddIngredients() {
        FoodInventory foodInventory = new FoodInventory();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        foodInventory.addIngredient(zucchini, 2);
        
        Map<Ingredient, Float> ingredientList = foodInventory.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(2.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testSubstractIngredients() {
        FoodInventory foodInventory = new FoodInventory();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        foodInventory.addIngredient(zucchini, 3);
        foodInventory.addIngredient(zucchini, -2);
        
        Map<Ingredient, Float> ingredientList = foodInventory.getIngredientsWithQuantity();
        Assert.assertEquals(1, ingredientList.size());
        Assert.assertEquals(1.0f, ingredientList.get(zucchini), 0.00001);
    }
    
    @Test
    public void testRemoveIngredients() {
        FoodInventory foodInventory = new FoodInventory();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        foodInventory.addIngredient(zucchini, 3);
        foodInventory.addIngredient(zucchini, -3);
        
        Map<Ingredient, Float> ingredientList = foodInventory.getIngredientsWithQuantity();
        Assert.assertEquals(0, ingredientList.size());
    }
    
    @Test
    public void testToString() {
        final String expectedString = "Noodles: 2 kg\r\nTomato: 6 \r\nWheat: 5 kg\r\nZucchini: 3 \r\n";
        FoodInventory foodInventory = new FoodInventory();
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        Ingredient noodles = new Ingredient("Noodles", Unit.KILOGRAMM, false);
        Ingredient tomtato = new Ingredient("Tomato", Unit.NONE, false);
        Ingredient wheat = new Ingredient("Wheat", Unit.KILOGRAMM, false);
        foodInventory.addIngredient(zucchini, 3);
        foodInventory.addIngredient(noodles, 2);
        foodInventory.addIngredient(tomtato, 6);
        foodInventory.addIngredient(wheat, 5);
        Assert.assertEquals(expectedString, foodInventory.toString());
    }
}

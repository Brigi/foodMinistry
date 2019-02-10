package org.food.ministry.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class TestRecipe {

    @Test
    public void testGetNameAndDescription() {
        final String name = "Al forno";
        final String description = "A very tasty noodle recipe.";
        Recipe recipe = new Recipe(name, new HashMap<Ingredient, Float>(), description);
        Assert.assertEquals(name, recipe.getName());
        Assert.assertEquals(description, recipe.getDescription());
    }
    
    @Test
    public void testSetNameAndDescription() {
        final String name = "Al forno";
        final String description = "A very tasty noodle recipe.";
        Recipe recipe = new Recipe("Schnitzel with French Fries", new HashMap<Ingredient, Float>(), "A flattend pork steak with french fries.");
        recipe.setName(name);
        recipe.setDescription(description);
        Assert.assertEquals(name, recipe.getName());
        Assert.assertEquals(description, recipe.getDescription());
    }
    
    @Test
    public void testGetIngredients() {
        Map<Ingredient, Float> ingredients = new HashMap<>();
        Ingredient noodles = new Ingredient("Noodles", Unit.KILOGRAMM, false);
        Ingredient cheese = new Ingredient("Cheese", Unit.GRAMM, false);
        Ingredient tomatoSauce = new Ingredient("Tomato sauce", Unit.MILILITER, false);
        Ingredient salt = new Ingredient("Salt", Unit.TEASPOON, true);
        ingredients.put(noodles, 0.5f);
        ingredients.put(cheese, 30f);
        ingredients.put(tomatoSauce, 300f);
        ingredients.put(salt, 1f);
        Recipe recipe = new Recipe("Al forno", ingredients, "A very tasty noodle recipe.");
        Map<Ingredient, Float> ingredientsToTest = recipe.getIngredientsWithQuantity();
        Assert.assertTrue(ingredientsToTest.containsKey(noodles));
        Assert.assertTrue(ingredientsToTest.containsKey(cheese));
        Assert.assertTrue(ingredientsToTest.containsKey(tomatoSauce));
        Assert.assertTrue(ingredientsToTest.containsKey(salt));
    }
    
    @Test
    public void testSetIngredients() {
        Recipe recipe = new Recipe("Al forno", null, "A very tasty noodle recipe.");
        Map<Ingredient, Float> ingredients = new HashMap<>();
        Ingredient noodles = new Ingredient("Noodles", Unit.KILOGRAMM, false);
        Ingredient cheese = new Ingredient("Cheese", Unit.GRAMM, false);
        Ingredient tomatoSauce = new Ingredient("Tomato sauce", Unit.MILILITER, false);
        Ingredient salt = new Ingredient("Salt", Unit.TEASPOON, true);
        ingredients.put(noodles, 0.5f);
        ingredients.put(cheese, 30f);
        ingredients.put(tomatoSauce, 300f);
        ingredients.put(salt, 1f);
        recipe.setIngredientsWithQuantity(ingredients);
        Map<Ingredient, Float> ingredientsToTest = recipe.getIngredientsWithQuantity();
        Assert.assertTrue(ingredientsToTest.containsKey(noodles));
        Assert.assertTrue(ingredientsToTest.containsKey(cheese));
        Assert.assertTrue(ingredientsToTest.containsKey(tomatoSauce));
        Assert.assertTrue(ingredientsToTest.containsKey(salt));
    }
}

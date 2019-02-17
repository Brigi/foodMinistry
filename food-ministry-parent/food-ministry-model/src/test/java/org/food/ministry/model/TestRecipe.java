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
        Recipe recipe = new Recipe(0, name, new HashMap<Ingredient, Float>(), description);
        Assert.assertEquals(name, recipe.getName());
        Assert.assertEquals(description, recipe.getDescription());
    }

    @Test
    public void testSetNameAndDescription() {
        final String name = "Al forno";
        final String description = "A very tasty noodle recipe.";
        Recipe recipe = new Recipe(0, "Schnitzel with French Fries", new HashMap<Ingredient, Float>(), "A flattend pork steak with french fries.");
        recipe.setName(name);
        recipe.setDescription(description);
        Assert.assertEquals(name, recipe.getName());
        Assert.assertEquals(description, recipe.getDescription());
    }

    @Test
    public void testGetIngredients() {
        Map<Ingredient, Float> ingredients = new HashMap<>();
        Ingredient noodles = new Ingredient(0, "Noodles", Unit.KILOGRAMM, false);
        Ingredient cheese = new Ingredient(1, "Cheese", Unit.GRAMM, false);
        Ingredient tomatoSauce = new Ingredient(2, "Tomato sauce", Unit.MILILITER, false);
        Ingredient salt = new Ingredient(3, "Salt", Unit.TEASPOON, true);
        ingredients.put(noodles, 0.5f);
        ingredients.put(cheese, 30f);
        ingredients.put(tomatoSauce, 300f);
        ingredients.put(salt, 1f);
        Recipe recipe = new Recipe(0, "Al forno", ingredients, "A very tasty noodle recipe.");
        Map<Ingredient, Float> ingredientsToTest = recipe.getIngredientsWithQuantity();
        Assert.assertTrue(ingredientsToTest.containsKey(noodles));
        Assert.assertTrue(ingredientsToTest.containsKey(cheese));
        Assert.assertTrue(ingredientsToTest.containsKey(tomatoSauce));
        Assert.assertTrue(ingredientsToTest.containsKey(salt));
    }

    @Test
    public void testSetIngredients() {
        Recipe recipe = new Recipe(0, "Al forno", null, "A very tasty noodle recipe.");
        Map<Ingredient, Float> ingredients = new HashMap<>();
        Ingredient noodles = new Ingredient(0, "Noodles", Unit.KILOGRAMM, false);
        Ingredient cheese = new Ingredient(1, "Cheese", Unit.GRAMM, false);
        Ingredient tomatoSauce = new Ingredient(2, "Tomato sauce", Unit.MILILITER, false);
        Ingredient salt = new Ingredient(3, "Salt", Unit.TEASPOON, true);
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

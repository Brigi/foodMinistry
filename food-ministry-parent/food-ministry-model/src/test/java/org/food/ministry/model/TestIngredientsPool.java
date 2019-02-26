package org.food.ministry.model;

import java.util.HashSet;
import java.util.Set;

import org.food.ministry.model.exception.IngredientNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestIngredientsPool {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private IngredientsPool ingredientsPool;

    @Before
    public void startUp() {
        ingredientsPool = new IngredientsPool(0);
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(0, ingredientsPool.getId());
    }

    @Test
    public void testAddIngredient() throws IngredientNotFoundException {
        final String ingredientName = "Zucchini";
        Ingredient ingredient = new Ingredient(0, ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        Assert.assertEquals(ingredientName, ingredientsPool.getIngredient(ingredientName).getName());
    }

    @Test
    public void testAddExistingIngredient() throws IngredientNotFoundException {
        final String ingredientName = "Zucchini";
        Ingredient ingredient = new Ingredient(0, ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        ingredientsPool.addIngredient(ingredient);
        Assert.assertEquals(ingredientName, ingredientsPool.getIngredient(ingredientName).getName());
        Assert.assertEquals(1, ingredientsPool.getIngredients().size());
    }
    
    @Test
    public void testSetIngredients() throws IngredientNotFoundException {
        final String ingredientName = "Zucchini";
        Ingredient ingredient = new Ingredient(0, ingredientName, Unit.NONE, false);
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient);
        
        ingredientsPool.setIngredients(ingredients);
        Assert.assertEquals(ingredientName, ingredientsPool.getIngredient(ingredientName).getName());
    }

    @Test
    public void testRemoveIngredient() {
        final String ingredientName = "Zucchini";
        Ingredient ingredient = new Ingredient(0, ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        ingredientsPool.removeIngredient(ingredient);
        Assert.assertTrue(ingredientsPool.getIngredients().isEmpty());
    }

    @Test
    public void testRemoveNonExistingIngredient() {
        final String ingredientName = "Zucchini";
        Ingredient ingredient = new Ingredient(0, ingredientName, Unit.NONE, false);
        ingredientsPool.removeIngredient(ingredient);
        Assert.assertTrue(ingredientsPool.getIngredients().isEmpty());
    }

    @Test
    public void testIngredientNotFoundException() throws IngredientNotFoundException {
        thrown.expect(IngredientNotFoundException.class);
        thrown.expectMessage("The ingredient 'Zucchini' was not found!");
        final String ingredientName = "Zucchini";
        ingredientsPool.getIngredient(ingredientName);
    }

    @Test
    public void testNull() throws IngredientNotFoundException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't search for ingredient given as 'null'");
        ingredientsPool.getIngredient(null);
    }
}

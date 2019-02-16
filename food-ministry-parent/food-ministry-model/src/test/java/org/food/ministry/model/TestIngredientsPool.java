package org.food.ministry.model;

import org.food.ministry.model.exception.IngredientNotFoundException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestIngredientsPool {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAddIngredient() throws IngredientNotFoundException {
        final String ingredientName = "Zucchini";
        IngredientsPool ingredientsPool = new IngredientsPool();
        Ingredient ingredient = new Ingredient(ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        Assert.assertEquals(ingredientName, ingredientsPool.getIngredient(ingredientName).getName());
    }
    
    @Test
    public void testAddExistingIngredient() throws IngredientNotFoundException {
        final String ingredientName = "Zucchini";
        IngredientsPool ingredientsPool = new IngredientsPool();
        Ingredient ingredient = new Ingredient(ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        ingredientsPool.addIngredient(ingredient);
        Assert.assertEquals(ingredientName, ingredientsPool.getIngredient(ingredientName).getName());
        Assert.assertEquals(1, ingredientsPool.getIngredients().size());
    }
    
    @Test
    public void testRemoveIngredient() {
        final String ingredientName = "Zucchini";
        IngredientsPool ingredientsPool = new IngredientsPool();
        Ingredient ingredient = new Ingredient(ingredientName, Unit.NONE, false);
        ingredientsPool.addIngredient(ingredient);
        ingredientsPool.removeIngredient(ingredient);
        Assert.assertTrue(ingredientsPool.getIngredients().isEmpty());
    }
    
    @Test
    public void testRemoveNonExistingIngredient() {
        final String ingredientName = "Zucchini";
        IngredientsPool ingredientsPool = new IngredientsPool();
        Ingredient ingredient = new Ingredient(ingredientName, Unit.NONE, false);
        ingredientsPool.removeIngredient(ingredient);
        Assert.assertTrue(ingredientsPool.getIngredients().isEmpty());
    }
    
    @Test
    public void testIngredientNotFoundException() throws IngredientNotFoundException {
        thrown.expect(IngredientNotFoundException.class);
        thrown.expectMessage("The ingredient 'Zucchini' was not found!");
        final String ingredientName = "Zucchini";
        IngredientsPool ingredientsPool = new IngredientsPool();
        ingredientsPool.getIngredient(ingredientName);
    }
    
    @Test
    public void testNull() throws IngredientNotFoundException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't search for ingredient given as 'null'");
        IngredientsPool ingredientsPool = new IngredientsPool();
        ingredientsPool.getIngredient(null);
    }
}

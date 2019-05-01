package org.food.ministry.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.food.ministry.model.exception.RecipeNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestRecipesPool {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RecipesPool recipesPool;

    @Before
    public void startUp() {
        recipesPool = new RecipesPool(0);
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(0, recipesPool.getId());
    }

    @Test
    public void testAddRecipe() throws RecipeNotFoundException {
        final String recipeName = "Al forno";
        Recipe recipe = new Recipe(0, recipeName, new HashMap<>(), "MyDescription");
        recipesPool.addRecipe(recipe);
        Assert.assertEquals(recipeName, recipesPool.getRecipe(recipeName).getName());
    }

    @Test
    public void testAddExistingRecipe() throws RecipeNotFoundException {
        final String recipeName = "Al forno";
        Recipe recipe = new Recipe(0, recipeName, new HashMap<>(), "MyDescription");
        recipesPool.addRecipe(recipe);
        recipesPool.addRecipe(recipe);
        Assert.assertEquals(recipeName, recipesPool.getRecipe(recipeName).getName());
        Assert.assertEquals(1, recipesPool.getRecipes().size());
    }
    
    @Test
    public void testSetRecipes() throws RecipeNotFoundException {
        final String recipeName = "Al forno";
        final String recipeDescription = "Tasty";
        Recipe recipe = new Recipe(0, recipeName, new HashMap<Ingredient, Float>(), recipeDescription);
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(recipe);
        
        recipesPool.setRecipes(recipes);
        Assert.assertEquals(recipeName, recipesPool.getRecipe(recipeName).getName());
    }

    @Test
    public void testRemoveRecipe() {
        final String recipeName = "Al forno";
        Recipe recipe = new Recipe(0, recipeName, new HashMap<>(), "MyDescription");
        recipesPool.addRecipe(recipe);
        recipesPool.removeRecipe(recipe);
        Assert.assertTrue(recipesPool.getRecipes().isEmpty());
    }

    @Test
    public void testRemoveNonExistingRecipe() {
        final String recipeName = "Al forno";
        Recipe recipe = new Recipe(0, recipeName, new HashMap<>(), "MyDescription");
        recipesPool.removeRecipe(recipe);
        Assert.assertTrue(recipesPool.getRecipes().isEmpty());
    }

    @Test
    public void testRecipeNotFoundException() throws RecipeNotFoundException {
        thrown.expect(RecipeNotFoundException.class);
        thrown.expectMessage("The recipe 'Al forno' was not found!");
        final String recipeName = "Al forno";
        recipesPool.getRecipe(recipeName);
    }

    @Test
    public void testNull() throws RecipeNotFoundException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't search for recipe given as 'null'");
        recipesPool.getRecipe(null);
    }
}

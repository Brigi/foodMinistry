package org.food.ministry.data.access.recipespool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.RecipesPool;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestRecipesPoolDAO extends TestBaseDAO<RecipesPool>{
    
    protected abstract RecipesPoolDAO getRecipesPoolDao();
    
    @Override
    protected RecipesPool getPersistenceObject() {
        return new RecipesPool(0);
    }

    @Override
    protected DAO<RecipesPool> getDao() {
        return getRecipesPoolDao();
    }

    @Test
    public void testUpdateIngredient() throws DataAccessException {
        final String recipeName = "MyIngredient";
        final Map<Ingredient, Float> ingredients = new HashMap<>();
        final String recipeDescription = "MyDescription";
        
        RecipesPool testIngredientsPool = getPersistenceObject();
        RecipesPoolDAO testIngredientsPoolDao = getRecipesPoolDao();

        Assert.assertEquals(0, testIngredientsPool.getRecipes().size());
        testIngredientsPoolDao.save(testIngredientsPool);
        testIngredientsPool.addRecipe(new Recipe(0, recipeName, ingredients, recipeDescription));
        testIngredientsPoolDao.update(testIngredientsPool);
        testIngredientsPool = testIngredientsPoolDao.get(testIngredientsPool.getId());
        Set<Recipe> recipes = testIngredientsPool.getRecipes();
        Assert.assertEquals(1, recipes.size());
        Recipe recipe = recipes.iterator().next();
        Assert.assertEquals(recipeName, recipe.getName());
        Assert.assertEquals(ingredients.size(), recipe.getIngredientsWithQuantity().size());
        Assert.assertEquals(recipeDescription, recipe.getDescription());
    }
}

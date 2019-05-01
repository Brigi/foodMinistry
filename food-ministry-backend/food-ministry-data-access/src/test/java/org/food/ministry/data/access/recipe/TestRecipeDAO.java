package org.food.ministry.data.access.recipe;

import java.util.HashMap;
import java.util.Map;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.Unit;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestRecipeDAO extends TestBaseDAO<Recipe>{

    private static final String RECIPE_NAME = "MyRecipe";
    private static final Map<Ingredient, Float> RECIPE_INGREDIENTS = new HashMap<>();
    private static final String RECIPE_DESCRIPTION = "MyDescription";

    protected abstract RecipeDAO getRecipeDao();
    
    @Override
    protected Recipe getPersistenceObject() {
        return new Recipe(0, RECIPE_NAME, RECIPE_INGREDIENTS, RECIPE_DESCRIPTION);
    }

    @Override
    protected DAO<Recipe> getDao() {
        return getRecipeDao();
    }

    @Test
    public void testUpdateIngredient() throws DataAccessException {
        final String newName = "NewName";
        final Map<Ingredient, Float> newIngredients = new HashMap<>();
        newIngredients.put(new Ingredient(0, "MyIngredient", Unit.NONE, false), 1f);
        final String newDescription = "NewDescription";
        
        Recipe testRecipe = getPersistenceObject();
        RecipeDAO testRecipeDao = getRecipeDao();

        testRecipeDao.save(testRecipe);
        testRecipe.setName(newName);
        testRecipe.setIngredientsWithQuantity(newIngredients);
        testRecipe.setDescription(newDescription);
        testRecipeDao.update(testRecipe);
        testRecipe = testRecipeDao.get(testRecipe.getId());
        Assert.assertEquals(newName, testRecipe.getName());
        Assert.assertEquals(newIngredients, testRecipe.getIngredientsWithQuantity());
        Assert.assertEquals(newDescription, testRecipe.getDescription());
    }
}

package org.food.ministry.data.access.ingredientspool;

import java.util.Set;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.Unit;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestIngredientsPoolDAO extends TestBaseDAO<IngredientsPool>{
    
    protected abstract IngredientsPoolDAO getIngredientsPoolDao();
    
    @Override
    protected IngredientsPool getPersistenceObject() {
        return new IngredientsPool(0);
    }

    @Override
    protected DAO<IngredientsPool> getDao() {
        return getIngredientsPoolDao();
    }

    @Test
    public void testUpdateIngredient() throws DataAccessException {
        final String ingredientName = "MyIngredient";
        final Unit ingredientUnit = Unit.NONE;
        final boolean ingredientIsBasic = false;
        
        IngredientsPool testIngredientsPool = getPersistenceObject();
        IngredientsPoolDAO testIngredientsPoolDao = getIngredientsPoolDao();

        Assert.assertEquals(0, testIngredientsPool.getIngredients().size());
        testIngredientsPoolDao.save(testIngredientsPool);
        testIngredientsPool.addIngredient(new Ingredient(0, ingredientName, ingredientUnit, ingredientIsBasic));
        testIngredientsPoolDao.update(testIngredientsPool);
        testIngredientsPool = testIngredientsPoolDao.get(testIngredientsPool.getId());
        Set<Ingredient> ingredients = testIngredientsPool.getIngredients();
        Assert.assertEquals(1, ingredients.size());
        Ingredient ingredient = ingredients.iterator().next();
        Assert.assertEquals(ingredientName, ingredient.getName());
        Assert.assertEquals(ingredientUnit, ingredient.getUnit());
        Assert.assertEquals(ingredientIsBasic, ingredient.isBasic());
    }
}

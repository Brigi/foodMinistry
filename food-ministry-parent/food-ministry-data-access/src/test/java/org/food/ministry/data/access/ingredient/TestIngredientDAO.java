package org.food.ministry.data.access.ingredient;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Unit;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestIngredientDAO extends TestBaseDAO<Ingredient>{

    protected abstract IngredientDAO getIngredientDao();

    private static final String INGREDIENT_NAME = "MyIngredient";
    private static final Unit INGREDIENT_UNIT = Unit.NONE;
    private static final boolean INGREDIENT_IS_BASIC = false;
    
    @Override
    protected Ingredient getPersistenceObject() {
        return new Ingredient(0, INGREDIENT_NAME, INGREDIENT_UNIT, INGREDIENT_IS_BASIC);
    }

    @Override
    protected DAO<Ingredient> getDao() {
        return getIngredientDao();
    }

    @Test
    public void testUpdateIngredient() throws DataAccessException {
        final String newName = "NewName";
        final Unit newUnit = Unit.GRAMM;
        final boolean newIsBasic = true;
        
        Ingredient testIngredient = getPersistenceObject();
        IngredientDAO testIngredientDao = getIngredientDao();

        testIngredientDao.save(testIngredient);
        testIngredient.setName(newName);
        testIngredient.setUnit(newUnit);
        testIngredient.setBasic(newIsBasic);
        testIngredientDao.update(testIngredient);
        testIngredient = testIngredientDao.get(testIngredient.getId());
        Assert.assertEquals(newName, testIngredient.getName());
        Assert.assertEquals(newUnit, testIngredient.getUnit());
        Assert.assertEquals(newIsBasic, testIngredient.isBasic());
    }
}

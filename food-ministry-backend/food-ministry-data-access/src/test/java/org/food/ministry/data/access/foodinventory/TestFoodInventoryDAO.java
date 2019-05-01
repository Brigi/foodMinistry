package org.food.ministry.data.access.foodinventory;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Unit;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestFoodInventoryDAO extends TestBaseDAO<FoodInventory>{

    protected abstract FoodInventoryDAO getFoodInventoryDao();

    @Override
    protected FoodInventory getPersistenceObject() {
        return new FoodInventory(0);
    }

    @Override
    protected DAO<FoodInventory> getDao() {
        return getFoodInventoryDao();
    }

    @Test
    public void testUpdateFoodInventory() throws DataAccessException {
        FoodInventory testFoodInventory = getPersistenceObject();
        FoodInventoryDAO testFoodInventoryDao = getFoodInventoryDao();
        Ingredient testIngredient = new Ingredient(0, "MyIngredient", Unit.NONE, false);

        Assert.assertEquals(0, testFoodInventory.getIngredientsWithQuantity().size());
        testFoodInventoryDao.save(testFoodInventory);
        testFoodInventory.addIngredient(testIngredient, 1);
        testFoodInventoryDao.update(testFoodInventory);
        testFoodInventory = testFoodInventoryDao.get(testFoodInventory.getId());
        Assert.assertEquals(1, testFoodInventory.getIngredientsWithQuantity().size());
    }
}

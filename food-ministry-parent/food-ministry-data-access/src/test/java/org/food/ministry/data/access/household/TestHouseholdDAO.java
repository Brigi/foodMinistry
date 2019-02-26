package org.food.ministry.data.access.household;

import java.util.Collection;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestHouseholdDAO extends TestBaseDAO<Household> {

    private static final String HOUSEHOLD_NAME = "HouseholdName";
    private static final FoodInventory FOOD_INVENTORY = new FoodInventory(0);
    private static final ShoppingList SHOPPING_LIST = new ShoppingList(0);
    private static final IngredientsPool INGREDIENTS_POOL = new IngredientsPool(0);
    private static final RecipesPool RECIPES_POOL = new RecipesPool(0);

    protected abstract HouseholdDAO getHouseholdDao();


    @Override
    protected Household getPersistenceObject() {
        return new Household(0, FOOD_INVENTORY, SHOPPING_LIST, INGREDIENTS_POOL, RECIPES_POOL, HOUSEHOLD_NAME);
    }

    @Override
    protected DAO<Household> getDao() {
        return getHouseholdDao();
    }
    
    @Test
    public void testUpdateHousehold() throws DataAccessException {
        final String newName = "newHouseholdName";
        Household testHousehold = getPersistenceObject();
        HouseholdDAO testHouseholdDao = getHouseholdDao();
        
        testHouseholdDao.save(testHousehold);
        testHousehold.setName(newName);
        testHouseholdDao.update(testHousehold);
        Collection<Household> households = testHouseholdDao.getAll();
        Assert.assertEquals(1, households.size());
        Assert.assertEquals(newName, households.iterator().next().getName());
    }

    @Test
    public void testUpdateWithMultipleHouseholdsContained() throws DataAccessException {
        final String newName = "newHouseholdName";
        Household testHousehold = getPersistenceObject();
        HouseholdDAO testHouseholdDao = getHouseholdDao();
        
        testHouseholdDao.save(testHousehold);
        testHouseholdDao.save(new Household(1, new FoodInventory(1), new ShoppingList(1), new IngredientsPool(1), new RecipesPool(1), "otherHousehold"));
        testHousehold.setName(newName);
        testHouseholdDao.update(testHousehold);
        Collection<Household> households = testHouseholdDao.getAll();
        Assert.assertEquals(2, households.size());
        long amountOfHouseholdsWithNewName = households.stream().filter(element -> element.getName().equals(newName)).count();
        Assert.assertEquals(1, amountOfHouseholdsWithNewName);
    }
}

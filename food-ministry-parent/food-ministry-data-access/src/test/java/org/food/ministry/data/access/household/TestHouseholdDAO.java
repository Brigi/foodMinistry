package org.food.ministry.data.access.household;

import java.text.MessageFormat;
import java.util.Collection;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.ShoppingList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public abstract class TestHouseholdDAO {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String HOUSEHOLD_NAME = "HouseholdName";
    private static final FoodInventory FOOD_INVENTORY = new FoodInventory(0);
    private static final ShoppingList SHOPPING_LIST = new ShoppingList(0);
    private static final IngredientsPool INGREDIENTS_POOL = new IngredientsPool(0);
    private Household testHousehold;
    private HouseholdDAO testHouseholdDao;

    protected abstract HouseholdDAO getHouseholdDao();

    @Before
    public void startUp() {
        testHousehold = new Household(0, FOOD_INVENTORY, SHOPPING_LIST, INGREDIENTS_POOL, HOUSEHOLD_NAME);
        testHouseholdDao = getHouseholdDao();
    }

    @Test
    public void testSaveHousehold() throws DataAccessException {
        testHouseholdDao.save(testHousehold);
    }
    
    @Test
    public void testGet() throws DataAccessException {
        testHouseholdDao.save(testHousehold);
        Assert.assertEquals(testHousehold, testHouseholdDao.get(testHousehold.getId()));
    }
    
    @Test
    public void testUpdateHousehold() throws DataAccessException {
        final String newName = "newHouseholdName";
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
        testHouseholdDao.save(testHousehold);
        testHouseholdDao.save(new Household(1, new FoodInventory(1), new ShoppingList(1), new IngredientsPool(1), "otherHousehold"));
        testHousehold.setName(newName);
        testHouseholdDao.update(testHousehold);
        Collection<Household> households = testHouseholdDao.getAll();
        Assert.assertEquals(2, households.size());
        long amountOfHouseholdsWithNewName = households.stream().filter(element -> element.getName().equals(newName)).count();
        Assert.assertEquals(1, amountOfHouseholdsWithNewName);
    }
    
    @Test
    public void testDeleteHousehold() throws DataAccessException {
        testHouseholdDao.save(testHousehold);
        testHouseholdDao.delete(testHousehold);
        Assert.assertTrue(testHouseholdDao.getAll().isEmpty());
    }

    @Test
    public void testDoesIdExistsPositive() throws DataAccessException {
        testHouseholdDao.save(testHousehold);
        Assert.assertTrue(testHouseholdDao.doesIdExist(0));
    }

    @Test
    public void testDoesIdExistsNegative() throws DataAccessException {
        testHouseholdDao.save(testHousehold);
        Assert.assertFalse(testHouseholdDao.doesIdExist(1));
    }

    @Test
    public void testGetNonExistingID() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(HouseholdDAO.NO_ID_FOUND_MESSAGE, 0));
        testHouseholdDao.get(0);
    }
    
    @Test
    public void testUpdateNonExistingHousehold() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(HouseholdDAO.INSUFFICIENT_AMOUNT_MESSAGE, 0));
        testHouseholdDao.update(testHousehold);
    }

    @Test
    public void testDeleteNonExistingHousehold() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(HouseholdDAO.NO_HOUSEHOLD_FOUND_MESSAGE, testHousehold.getName()));
        testHouseholdDao.delete(testHousehold);
    }
}

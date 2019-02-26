package org.food.ministry.data.access.shoppinglist;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.ShoppingList;
import org.food.ministry.model.Unit;
import org.junit.Assert;
import org.junit.Test;

public abstract class TestShoppingListDAO extends TestBaseDAO<ShoppingList>{

    protected abstract ShoppingListDAO getShoppingListDao();

    @Override
    protected ShoppingList getPersistenceObject() {
        return new ShoppingList(0);
    }

    @Override
    protected DAO<ShoppingList> getDao() {
        return getShoppingListDao();
    }

    @Test
    public void testUpdateShoppingList() throws DataAccessException {
        ShoppingList testShoppingList = getPersistenceObject();
        ShoppingListDAO testShoppingListDao = getShoppingListDao();
        Ingredient testIngredient = new Ingredient(0, "MyIngredient", Unit.NONE, false);

        Assert.assertEquals(0, testShoppingList.getIngredientsWithQuantity().size());
        testShoppingListDao.save(testShoppingList);
        testShoppingList.addIngredient(testIngredient, 1);
        testShoppingListDao.update(testShoppingList);
        testShoppingList = testShoppingListDao.get(testShoppingList.getId());
        Assert.assertEquals(1, testShoppingList.getIngredientsWithQuantity().size());
    }
}

package org.food.ministry.data.access.shoppinglist;

public class TestInMemoryShoppingListDAO extends TestShoppingListDAO {

    @Override
    protected ShoppingListDAO getShoppingListDao() {
        return new InMemoryShoppingListDAO();
    }
}

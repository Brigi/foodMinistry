package org.food.ministry.data.access.shoppinglist;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.ShoppingList;

public class InMemoryShoppingListDAO extends InMemoryBaseDAO<ShoppingList> implements ShoppingListDAO {

    @Override
    public void update(ShoppingList shoppingList) throws DataAccessException {
        ShoppingList shoppinhListToUpdate = getItemToUpdate(shoppingList);
        shoppinhListToUpdate.setIngredientsWithQuantity(shoppingList.getIngredientsWithQuantity());
    }
}

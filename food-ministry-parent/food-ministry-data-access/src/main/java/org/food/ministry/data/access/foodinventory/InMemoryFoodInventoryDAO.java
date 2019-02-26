package org.food.ministry.data.access.foodinventory;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;

public class InMemoryFoodInventoryDAO extends InMemoryBaseDAO<FoodInventory> implements FoodInventoryDAO {

    @Override
    public void update(FoodInventory foodInventory) throws DataAccessException {
        FoodInventory foodInventoryToUpdate = getItemToUpdate(foodInventory);
        foodInventoryToUpdate.setIngredientsWithQuantity(foodInventory.getIngredientsWithQuantity());
    }
}

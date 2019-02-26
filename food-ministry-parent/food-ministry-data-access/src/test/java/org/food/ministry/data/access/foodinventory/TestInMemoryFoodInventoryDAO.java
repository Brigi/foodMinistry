package org.food.ministry.data.access.foodinventory;

public class TestInMemoryFoodInventoryDAO extends TestFoodInventoryDAO {

    @Override
    protected FoodInventoryDAO getFoodInventoryDao() {
        return new InMemoryFoodInventoryDAO();
    }
}

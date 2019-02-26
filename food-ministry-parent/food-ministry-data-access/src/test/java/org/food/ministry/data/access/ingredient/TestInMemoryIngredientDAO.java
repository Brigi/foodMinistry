package org.food.ministry.data.access.ingredient;

public class TestInMemoryIngredientDAO extends TestIngredientDAO {

    @Override
    protected IngredientDAO getIngredientDao() {
        return new InMemoryIngredientDAO();
    }
}

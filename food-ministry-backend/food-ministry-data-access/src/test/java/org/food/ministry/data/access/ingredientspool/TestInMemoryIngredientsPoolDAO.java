package org.food.ministry.data.access.ingredientspool;

public class TestInMemoryIngredientsPoolDAO extends TestIngredientsPoolDAO {

    @Override
    protected IngredientsPoolDAO getIngredientsPoolDao() {
        return new InMemoryIngredientsPoolDAO();
    }
}

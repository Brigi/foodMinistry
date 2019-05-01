package org.food.ministry.data.access.recipespool;

public class TestInMemoryRecipesPoolDAO extends TestRecipesPoolDAO {

    @Override
    protected RecipesPoolDAO getRecipesPoolDao() {
        return new InMemoryRecipesPoolDAO();
    }
}

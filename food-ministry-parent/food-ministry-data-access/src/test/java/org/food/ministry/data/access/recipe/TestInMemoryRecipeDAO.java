package org.food.ministry.data.access.recipe;

public class TestInMemoryRecipeDAO extends TestRecipeDAO {

    @Override
    protected RecipeDAO getRecipeDao() {
        return new InMemoryRecipeDAO();
    }
}

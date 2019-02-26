package org.food.ministry.data.access.recipespool;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.RecipesPool;

public class InMemoryRecipesPoolDAO extends InMemoryBaseDAO<RecipesPool> implements RecipesPoolDAO {

    @Override
    public void update(RecipesPool recipesPool) throws DataAccessException {
        RecipesPool recipesPoolToUpdate = getItemToUpdate(recipesPool);
        recipesPoolToUpdate.setRecipes(recipesPool.getRecipes());
    }
}

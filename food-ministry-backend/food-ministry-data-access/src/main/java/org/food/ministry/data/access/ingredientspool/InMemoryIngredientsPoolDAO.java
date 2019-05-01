package org.food.ministry.data.access.ingredientspool;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.IngredientsPool;

public class InMemoryIngredientsPoolDAO extends InMemoryBaseDAO<IngredientsPool> implements IngredientsPoolDAO {

    @Override
    public void update(IngredientsPool ingredientsPool) throws DataAccessException {
        IngredientsPool ingredientsPoolToUpdate = getItemToUpdate(ingredientsPool);
        ingredientsPoolToUpdate.setIngredients(ingredientsPool.getIngredients());
    }
}

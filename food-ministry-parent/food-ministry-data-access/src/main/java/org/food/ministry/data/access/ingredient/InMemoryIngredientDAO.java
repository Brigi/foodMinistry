package org.food.ministry.data.access.ingredient;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Ingredient;

public class InMemoryIngredientDAO extends InMemoryBaseDAO<Ingredient> implements IngredientDAO {

    @Override
    public void update(Ingredient ingredient) throws DataAccessException {
        Ingredient ingredientToUpdate = getItemToUpdate(ingredient);
        ingredientToUpdate.setName(ingredient.getName());
        ingredientToUpdate.setUnit(ingredient.getUnit());
        ingredientToUpdate.setBasic(ingredient.isBasic());
    }
}

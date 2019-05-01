package org.food.ministry.data.access.recipe;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Recipe;

public class InMemoryRecipeDAO extends InMemoryBaseDAO<Recipe> implements RecipeDAO {
    
    @Override
    public void update(Recipe recipe) throws DataAccessException {
        Recipe recipeToUpdate = getItemToUpdate(recipe);
        recipeToUpdate.setName(recipe.getName());
        recipeToUpdate.setIngredientsWithQuantity(recipe.getIngredientsWithQuantity());
        recipeToUpdate.setDescription(recipe.getDescription());
    }
}

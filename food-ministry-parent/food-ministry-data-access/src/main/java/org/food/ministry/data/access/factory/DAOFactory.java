package org.food.ministry.data.access.factory;

import org.food.ministry.data.access.StorageType;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;

public abstract class DAOFactory {

    abstract FoodInventoryDAO getFoodInventoryDAO();
    
    abstract HouseholdDAO getHouseholdDAO();
    
    abstract IngredientDAO getIngredientDAO();
    
    abstract IngredientsPoolDAO getIngredientsPoolDAO();
    
    abstract RecipeDAO getRecipeDAO();
    
    abstract RecipesPoolDAO getRecipesPoolDAO();
    
    abstract ShoppingListDAO getShoppingListDAO();
    
    abstract UserDAO getUserDAO();
    
    public static DAOFactory getDAOFactory(StorageType storageType) {
        switch(storageType) {
            case INMEMORY:
                return new InMemoryDAOFactory();
            default:
                return new InMemoryDAOFactory();
        }
    }
}

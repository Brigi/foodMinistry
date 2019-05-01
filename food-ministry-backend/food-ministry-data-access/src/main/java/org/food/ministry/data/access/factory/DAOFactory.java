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

    public abstract FoodInventoryDAO getFoodInventoryDAO();
    
    public abstract HouseholdDAO getHouseholdDAO();
    
    public abstract IngredientDAO getIngredientDAO();
    
    public abstract IngredientsPoolDAO getIngredientsPoolDAO();
    
    public abstract RecipeDAO getRecipeDAO();
    
    public abstract RecipesPoolDAO getRecipesPoolDAO();
    
    public abstract ShoppingListDAO getShoppingListDAO();
    
    public abstract UserDAO getUserDAO();
    
    public static DAOFactory getDAOFactory(StorageType storageType) {
        switch(storageType) {
            case INMEMORY:
                return new InMemoryDAOFactory();
            default:
                return new InMemoryDAOFactory();
        }
    }
}

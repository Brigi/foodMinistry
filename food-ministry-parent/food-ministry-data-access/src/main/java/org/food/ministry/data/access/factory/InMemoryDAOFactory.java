package org.food.ministry.data.access.factory;

import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.foodinventory.InMemoryFoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.household.InMemoryHouseholdDAO;
import org.food.ministry.data.access.ingredient.InMemoryIngredientDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.InMemoryIngredientsPoolDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.recipe.InMemoryRecipeDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.InMemoryRecipesPoolDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.data.access.shoppinglist.InMemoryShoppingListDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.InMemoryUserDAO;
import org.food.ministry.data.access.users.UserDAO;

public class InMemoryDAOFactory extends DAOFactory {

    private InMemoryFoodInventoryDAO foodInventoryDao;
    private InMemoryHouseholdDAO householdDao;
    private InMemoryIngredientDAO ingredientDao;
    private InMemoryIngredientsPoolDAO ingredientsPoolDao;
    private InMemoryRecipeDAO recipeDao;
    private InMemoryRecipesPoolDAO recipesPoolDao;
    private InMemoryShoppingListDAO shoppingListDao;
    private InMemoryUserDAO userDao;
    
    public InMemoryDAOFactory() {
        foodInventoryDao = new InMemoryFoodInventoryDAO();
        householdDao = new InMemoryHouseholdDAO();
        ingredientDao = new InMemoryIngredientDAO();
        ingredientsPoolDao = new InMemoryIngredientsPoolDAO();
        recipeDao = new InMemoryRecipeDAO();
        recipesPoolDao = new InMemoryRecipesPoolDAO();
        shoppingListDao = new InMemoryShoppingListDAO();
        userDao = new InMemoryUserDAO();
    }
    
    @Override
    FoodInventoryDAO getFoodInventoryDAO() {
        return foodInventoryDao;
    }

    @Override
    HouseholdDAO getHouseholdDAO() {
        return householdDao;
    }

    @Override
    IngredientDAO getIngredientDAO() {
        return ingredientDao;
    }

    @Override
    IngredientsPoolDAO getIngredientsPoolDAO() {
        return ingredientsPoolDao;
    }

    @Override
    RecipeDAO getRecipeDAO() {
        return recipeDao;
    }

    @Override
    RecipesPoolDAO getRecipesPoolDAO() {
        return recipesPoolDao;
    }

    @Override
    ShoppingListDAO getShoppingListDAO() {
        return shoppingListDao;
    }

    @Override
    UserDAO getUserDAO() {
        return userDao;
    }

}

package org.food.ministry.data.access;

import org.food.ministry.data.access.foodinventory.TestInMemoryFoodInventoryDAO;
import org.food.ministry.data.access.household.TestInMemoryHouseholdDAO;
import org.food.ministry.data.access.ingredient.TestInMemoryIngredientDAO;
import org.food.ministry.data.access.ingredientspool.TestInMemoryIngredientsPoolDAO;
import org.food.ministry.data.access.recipe.TestInMemoryRecipeDAO;
import org.food.ministry.data.access.recipespool.TestInMemoryRecipesPoolDAO;
import org.food.ministry.data.access.shoppinglist.TestInMemoryShoppingListDAO;
import org.food.ministry.data.access.users.TestInMemoryUserDAO;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestInMemoryFoodInventoryDAO.class, TestInMemoryHouseholdDAO.class, TestInMemoryIngredientDAO.class, TestInMemoryIngredientsPoolDAO.class,
        TestInMemoryRecipeDAO.class, TestInMemoryRecipesPoolDAO.class, TestInMemoryShoppingListDAO.class, TestInMemoryUserDAO.class })

public class AllTests {

}

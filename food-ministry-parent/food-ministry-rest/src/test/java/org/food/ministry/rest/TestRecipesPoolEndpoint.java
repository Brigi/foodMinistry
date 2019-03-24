package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.model.Unit;
import org.food.ministry.rest.household.json.GetIngredientsPoolResultJSON;
import org.food.ministry.rest.household.json.GetRecipesPoolResultJSON;
import org.food.ministry.rest.ingredientspool.json.GetIngredientsResultJSON;
import org.food.ministry.rest.recipe.json.GetRecipeResultJSON;
import org.food.ministry.rest.recipespool.json.GetRecipesResultJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.HouseholdUtil;
import org.food.ministry.rest.util.IngredientsPoolUtil;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.RecipeUtil;
import org.food.ministry.rest.util.RecipesPoolUtil;
import org.food.ministry.rest.util.TestData;
import org.food.ministry.rest.util.UserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestRecipesPoolEndpoint extends ServerManagerTestBase {

    private long userId;

    private long ingredientsPoolId;
    
    private long recipesPoolId;
    
    @Before
    public void setupHousehold() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        // Register and login user
        UserUtil.registerUser(TestData.USER_NAME, TestData.USER_ADDRESS, TestData.USER_PASSWORD);
        HttpResponse loginResponse = UserUtil.loginUser(TestData.USER_ADDRESS, TestData.USER_PASSWORD);
        LoginUserResultJSON loginResult = MessagesUtil.extractJSONFromResponse(loginResponse, LoginUserResultJSON.class);
        
        // Get Household
        UserUtil.addHousehold(loginResult.getUserId(), TestData.HOUSEHOLD_NAME);
        HttpResponse getHouseholdsResponse = UserUtil.getHouseholds(loginResult.getUserId());
        GetHouseholdsResultJSON getHouseholdsResult = MessagesUtil.extractJSONFromResponse(getHouseholdsResponse, GetHouseholdsResultJSON.class);
        long householdId = getHouseholdsResult.getHouseholds().keySet().iterator().next();
        userId = loginResult.getUserId();
        
        // Get Ingredients Pool
        HttpResponse getIngredientsPoolResponse = HouseholdUtil.getIngredientsPool(userId, householdId);
        GetIngredientsPoolResultJSON getIngredientsPoolResult = MessagesUtil.extractJSONFromResponse(getIngredientsPoolResponse, GetIngredientsPoolResultJSON.class);
        ingredientsPoolId = getIngredientsPoolResult.getIngredientsPoolId();
        
        // Get Recipes Pool
        HttpResponse getRecipesPoolResponse = HouseholdUtil.getRecipesPool(userId, householdId);
        GetRecipesPoolResultJSON getRecipesPoolResult = MessagesUtil.extractJSONFromResponse(getRecipesPoolResponse, GetRecipesPoolResultJSON.class);
        recipesPoolId = getRecipesPoolResult.getRecipesPoolId();
    }
    
    @Test
    public void testAddAndGetRecipe() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        final String ingredientName = "MyIngredient";
        final Unit ingredientUnit = Unit.LITER;
        final boolean isIngredientBasic = false;
        
        final String recipeName = "MyRecipe";
        final String recipeDescription = "My Description";
        final Map<Long, Float> ingredientsWithAmount = new HashMap<>();
        final float ingredientAmount = 2.5f;
        
        // Add and get Ingredient
        IngredientsPoolUtil.addIngredient(userId, ingredientsPoolId, ingredientName, ingredientUnit, isIngredientBasic);
        HttpResponse getIngredientsResponse = IngredientsPoolUtil.getIngredients(userId, ingredientsPoolId);
        GetIngredientsResultJSON getIngredientsResult = MessagesUtil.extractJSONFromResponse(getIngredientsResponse, GetIngredientsResultJSON.class);
        Set<Long> ingredientIds = getIngredientsResult.getIngredientIds();
        long ingredientId = ingredientIds.iterator().next();
        
        // Add Recipe
        ingredientsWithAmount.put(ingredientId, ingredientAmount);
        HttpResponse addRecipeResponse = RecipesPoolUtil.addRecipe(userId, recipesPoolId, recipeName, ingredientsWithAmount, recipeDescription);
        Assert.assertEquals(StatusCodes.CREATED, addRecipeResponse.status());
        
        // Get Recipe
        HttpResponse getRecipesResponse = RecipesPoolUtil.getRecipes(userId, recipesPoolId);
        Assert.assertEquals(StatusCodes.OK, getRecipesResponse.status());
        GetRecipesResultJSON getRecipesResult = MessagesUtil.extractJSONFromResponse(getRecipesResponse, GetRecipesResultJSON.class);
        Map<Long, String> recipesWithName = getRecipesResult.getRecipesWithName();
        Assert.assertEquals(1, recipesWithName.size());
        Assert.assertEquals(recipeName, recipesWithName.values().iterator().next());
        
        HttpResponse getRecipeResponse = RecipeUtil.getRecipe(userId, recipesWithName.keySet().iterator().next());
        Assert.assertEquals(StatusCodes.OK, getRecipeResponse.status());
        GetRecipeResultJSON getRecipeResult = MessagesUtil.extractJSONFromResponse(getRecipeResponse, GetRecipeResultJSON.class);
        Assert.assertEquals(recipeName, getRecipeResult.getRecipeName());
        Assert.assertEquals(recipeDescription, getRecipeResult.getRecipeDescription());
        Assert.assertEquals(1, getRecipeResult.getIngredientWithAmount().size());
        Assert.assertEquals(ingredientAmount, getRecipeResult.getIngredientWithAmount().get(ingredientId), 0.00001);
    }
}

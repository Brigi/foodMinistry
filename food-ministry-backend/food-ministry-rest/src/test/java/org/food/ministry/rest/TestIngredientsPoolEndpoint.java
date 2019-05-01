package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.model.Unit;
import org.food.ministry.rest.household.json.GetIngredientsPoolResultJSON;
import org.food.ministry.rest.ingredient.json.GetIngredientResultJSON;
import org.food.ministry.rest.ingredientspool.json.GetIngredientsResultJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.HouseholdUtil;
import org.food.ministry.rest.util.IngredientUtil;
import org.food.ministry.rest.util.IngredientsPoolUtil;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.TestData;
import org.food.ministry.rest.util.UserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestIngredientsPoolEndpoint extends ServerManagerTestBase {

    private long userId;
    
    private long ingredientsPoolId;
    
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
    }
    
    @Test
    public void testAddAndGetIngredient() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        final String ingredientName = "MyIngredient";
        final Unit ingredientUnit = Unit.LITER;
        final boolean isIngredientBasic = false;
        
        // Add Ingredient
        HttpResponse addIngredientResponse = IngredientsPoolUtil.addIngredient(userId, ingredientsPoolId, ingredientName, ingredientUnit, isIngredientBasic);
        Assert.assertEquals(StatusCodes.CREATED, addIngredientResponse.status());
        
        // Get Ingredient
        HttpResponse getIngredientsResponse = IngredientsPoolUtil.getIngredients(userId, ingredientsPoolId);
        Assert.assertEquals(StatusCodes.OK, getIngredientsResponse.status());
        
        GetIngredientsResultJSON getIngredientsResult = MessagesUtil.extractJSONFromResponse(getIngredientsResponse, GetIngredientsResultJSON.class);
        Set<Long> ingredientIds = getIngredientsResult.getIngredientIds();
        Assert.assertEquals(1, ingredientIds.size());
        long ingredientId = ingredientIds.iterator().next();
        HttpResponse getIngredientResponse = IngredientUtil.getIngredient(userId, ingredientId);
        Assert.assertEquals(StatusCodes.OK, getIngredientResponse.status());
        GetIngredientResultJSON getIngredientResult = MessagesUtil.extractJSONFromResponse(getIngredientResponse, GetIngredientResultJSON.class);
        Assert.assertEquals(ingredientName, getIngredientResult.getIngredientName());
        Assert.assertEquals(ingredientUnit, getIngredientResult.getIngredientUnit());
        Assert.assertEquals(isIngredientBasic, getIngredientResult.isIngredientBasic());
    }
}

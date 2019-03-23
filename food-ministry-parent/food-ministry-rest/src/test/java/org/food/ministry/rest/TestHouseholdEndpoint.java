package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.rest.household.json.GetFoodInventoryResultJSON;
import org.food.ministry.rest.household.json.GetIngredientsPoolResultJSON;
import org.food.ministry.rest.household.json.GetRecipesPoolResultJSON;
import org.food.ministry.rest.household.json.GetShoppingListResultJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.HouseholdUtil;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.TestData;
import org.food.ministry.rest.util.UserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestHouseholdEndpoint extends ServerManagerTestBase {

    private long userId;
    
    private long householdId;
    
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
        householdId = getHouseholdsResult.getHouseholds().keySet().iterator().next();
        userId = loginResult.getUserId();
    }
    
    @Test
    public void testGetFoodInventory() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse getFoodInventoryResponse = HouseholdUtil.getFoodInventory(userId, householdId);
        GetFoodInventoryResultJSON getFoodInventoryResult = MessagesUtil.extractJSONFromResponse(getFoodInventoryResponse, GetFoodInventoryResultJSON.class);
        Assert.assertEquals(StatusCodes.OK, getFoodInventoryResponse.status());
        Assert.assertNotEquals(0, getFoodInventoryResult.getFoodInventoryId());
    }
    
    @Test
    public void testGetIngredientsPool() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse getIngredientsPoolResponse = HouseholdUtil.getIngredientsPool(userId, householdId);
        GetIngredientsPoolResultJSON getIngredientsPoolResult = MessagesUtil.extractJSONFromResponse(getIngredientsPoolResponse, GetIngredientsPoolResultJSON.class);
        Assert.assertEquals(StatusCodes.OK, getIngredientsPoolResponse.status());
        Assert.assertNotEquals(0, getIngredientsPoolResult.getIngredientsPoolId());
    }
    
    @Test
    public void testGetRecipesPool() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse getRecipesPoolResponse = HouseholdUtil.getRecipesPool(userId, householdId);
        GetRecipesPoolResultJSON getRecipesPoolResult = MessagesUtil.extractJSONFromResponse(getRecipesPoolResponse, GetRecipesPoolResultJSON.class);
        Assert.assertEquals(StatusCodes.OK, getRecipesPoolResponse.status());
        Assert.assertNotEquals(0, getRecipesPoolResult.getRecipesPoolId());
    }
    
    @Test
    public void testGetShoppingList() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse getShoppingListResponse = HouseholdUtil.getShoppingList(userId, householdId);
        GetShoppingListResultJSON getShoppingListResult = MessagesUtil.extractJSONFromResponse(getShoppingListResponse, GetShoppingListResultJSON.class);
        Assert.assertEquals(StatusCodes.OK, getShoppingListResponse.status());
        Assert.assertNotEquals(0, getShoppingListResult.getShoppingListId());
    }
}

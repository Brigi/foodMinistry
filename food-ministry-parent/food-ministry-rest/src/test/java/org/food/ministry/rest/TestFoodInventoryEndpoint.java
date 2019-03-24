package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.model.Unit;
import org.food.ministry.rest.household.json.GetFoodInventoryResultJSON;
import org.food.ministry.rest.household.json.GetIngredientsPoolResultJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.FoodInventoryUtil;
import org.food.ministry.rest.util.HouseholdUtil;
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

public class TestFoodInventoryEndpoint extends ServerManagerTestBase {

    private long userId;

    private long ingredientsPoolId;

    private long foodInventoryId;

    private long ingredientId;

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

        // Add Ingredient to Ingredients Pool
        HttpResponse getIngredientsPoolResponse = HouseholdUtil.getIngredientsPool(userId, householdId);
        GetIngredientsPoolResultJSON getIngredientsPoolResult = MessagesUtil.extractJSONFromResponse(getIngredientsPoolResponse, GetIngredientsPoolResultJSON.class);
        ingredientsPoolId = getIngredientsPoolResult.getIngredientsPoolId();
        IngredientsPoolUtil.addIngredient(userId, ingredientsPoolId, "MyIngredient", Unit.TABLESPOON, false);

        HttpResponse getIngredientsResponse = IngredientsPoolUtil.getIngredients(userId, ingredientsPoolId);
        org.food.ministry.rest.ingredientspool.json.GetIngredientsResultJSON getIngredientResult = MessagesUtil.extractJSONFromResponse(getIngredientsResponse,
                org.food.ministry.rest.ingredientspool.json.GetIngredientsResultJSON.class);
        ingredientId = getIngredientResult.getIngredientIds().iterator().next();

        // Get Food Inventory
        HttpResponse getFoodInventoryResponse = HouseholdUtil.getFoodInventory(userId, householdId);
        GetFoodInventoryResultJSON getFoodInventoryResult = MessagesUtil.extractJSONFromResponse(getFoodInventoryResponse, GetFoodInventoryResultJSON.class);
        foodInventoryId = getFoodInventoryResult.getFoodInventoryId();
    }

    @Test
    public void testAddAndGetIngredient() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        final float ingredientAmount = 2.0f;

        // Add Ingredient
        HttpResponse addIngredientResponse = FoodInventoryUtil.addIngredient(userId, foodInventoryId, ingredientId, ingredientAmount);
        Assert.assertEquals(StatusCodes.CREATED, addIngredientResponse.status());
        
        // Get Ingredient
        HttpResponse getIngredientsResponse = FoodInventoryUtil.getIngredients(userId, foodInventoryId);
        Assert.assertEquals(StatusCodes.OK, getIngredientsResponse.status());
        org.food.ministry.rest.foodinventory.json.GetIngredientsResultJSON getIngredientsResult = MessagesUtil.extractJSONFromResponse(getIngredientsResponse,
                org.food.ministry.rest.foodinventory.json.GetIngredientsResultJSON.class);
        Map<Long, Float> ingredientsWithAmount = getIngredientsResult.getIngredientsWithAmount();
        Assert.assertEquals(1, ingredientsWithAmount.size());
        Assert.assertEquals(ingredientAmount, ingredientsWithAmount.get(ingredientId), 0.00001);
    }
}

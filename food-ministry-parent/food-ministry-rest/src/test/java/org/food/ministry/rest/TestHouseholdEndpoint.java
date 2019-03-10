package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.rest.household.json.GetFoodInventoryResultJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.HouseholdUtil;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.TestData;
import org.food.ministry.rest.util.UserUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestHouseholdEndpoint extends ServerManagerTestBase {

    @Test
    public void testGetFoodInventory() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        // Register and login user
        UserUtil.registerUser(TestData.USER_NAME, TestData.USER_ADDRESS, TestData.USER_PASSWORD);
        HttpResponse loginResponse = UserUtil.loginUser(TestData.USER_ADDRESS, TestData.USER_PASSWORD);
        LoginUserResultJSON loginResult = MessagesUtil.extractJSONFromResponse(loginResponse, LoginUserResultJSON.class);
        
        // Get Household and Food Inventory
        UserUtil.addHousehold(loginResult.getUserId(), TestData.HOUSEHOLD_NAME);
        HttpResponse getHouseholdsResponse = UserUtil.getHouseholds(loginResult.getUserId());
        GetHouseholdsResultJSON getHouseholdsResult = MessagesUtil.extractJSONFromResponse(getHouseholdsResponse, GetHouseholdsResultJSON.class);
        long householdId = getHouseholdsResult.getHouseholds().keySet().iterator().next();
        HttpResponse getFoodInventoryResponse = HouseholdUtil.getFoodInventory(loginResult.getUserId(), householdId);
        GetFoodInventoryResultJSON getFoodInventoryResult = MessagesUtil.extractJSONFromResponse(getFoodInventoryResponse, GetFoodInventoryResultJSON.class);
        Assert.assertEquals(StatusCodes.OK, getFoodInventoryResponse.status());
        Assert.assertNotEquals(0, getFoodInventoryResult.getFoodInventoryId());
    }
}

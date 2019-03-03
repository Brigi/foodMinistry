package org.food.ministry.rest;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.rest.json.ErrorJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.UserUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestUserEndpoint extends ServerManagerTestBase {

    private static final String USER_NAME = "MyName";
    private static final String USER_ADDRESS = "email@address.com";
    private static final String USER_PASSWORD = "MyPassword";
    private static final String HOUSEHOLD_NAME = "MyHousehold";

    @Test
    public void testSuccessfulRegisterUserAndLogin() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        Assert.assertEquals(StatusCodes.CREATED, UserUtil.registerUser(USER_NAME, USER_ADDRESS, USER_PASSWORD).status());
        HttpResponse loginResponse = UserUtil.loginUser(USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.OK, loginResponse.status());
        LoginUserResultJSON loginResult = MessagesUtil.extractJSONFromResponse(loginResponse, LoginUserResultJSON.class);
        Assert.assertTrue(loginResult.getUserId() != 0);
    }

    @Test
    public void testFailedLogin() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse response = UserUtil.loginUser(USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.BAD_REQUEST, response.status());
        Assert.assertEquals(
                MessageFormat.format("Login for user {0} failed: ", USER_ADDRESS) + MessageFormat.format(UserDAO.NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, USER_ADDRESS),
                MessagesUtil.extractJSONFromResponse(response, ErrorJSON.class).getErrorMessage());
    }

    @Test
    public void testDoubleRegistration() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse response = UserUtil.registerUser(USER_NAME, USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.CREATED, response.status());
        response = UserUtil.registerUser(USER_NAME, USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.BAD_REQUEST, response.status());
        Assert.assertEquals(
                MessageFormat.format("Registration of user {0} failed: ", USER_NAME) + MessageFormat.format(UserDAO.USER_WITH_EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, USER_ADDRESS),
                MessagesUtil.extractJSONFromResponse(response, ErrorJSON.class).getErrorMessage());
    }
    
    @Test
    public void testGetEmptyHouseholds() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        UserUtil.registerUser(USER_NAME, USER_ADDRESS, USER_PASSWORD);
        HttpResponse loginResponse = UserUtil.loginUser(USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.OK, loginResponse.status());
        LoginUserResultJSON loginResult = MessagesUtil.extractJSONFromResponse(loginResponse, LoginUserResultJSON.class);
        HttpResponse getHouseholdsResponse = UserUtil.getHouseholds(loginResult.getUserId());
        Assert.assertEquals(StatusCodes.OK, getHouseholdsResponse.status());
        GetHouseholdsResultJSON getHouseholdsResult = MessagesUtil.extractJSONFromResponse(getHouseholdsResponse, GetHouseholdsResultJSON.class);
        Assert.assertTrue(getHouseholdsResult.getHouseholds().isEmpty());
    }
    
    @Test
    public void testAddGetAndRemoveHousehold() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        // Register and login user
        UserUtil.registerUser(USER_NAME, USER_ADDRESS, USER_PASSWORD);
        HttpResponse loginResponse = UserUtil.loginUser(USER_ADDRESS, USER_PASSWORD);
        Assert.assertEquals(StatusCodes.OK, loginResponse.status());
        LoginUserResultJSON loginResult = MessagesUtil.extractJSONFromResponse(loginResponse, LoginUserResultJSON.class);
        
        // Add household
        HttpResponse addHouseholdResponse = UserUtil.addHousehold(loginResult.getUserId(), HOUSEHOLD_NAME);
        Assert.assertEquals(StatusCodes.CREATED, addHouseholdResponse.status());
        
        // Get households
        HttpResponse getHouseholdsResponse = UserUtil.getHouseholds(loginResult.getUserId());
        Assert.assertEquals(StatusCodes.OK, getHouseholdsResponse.status());
        GetHouseholdsResultJSON getHouseholdsResult = MessagesUtil.extractJSONFromResponse(getHouseholdsResponse, GetHouseholdsResultJSON.class);
        Map<Long, String> households = getHouseholdsResult.getHouseholds();
        Assert.assertEquals(1, households.size());
        Assert.assertEquals(HOUSEHOLD_NAME, households.values().iterator().next());
        
        // Remove household
        HttpResponse removeHouseholdResponse = UserUtil.removeHousehold(loginResult.getUserId(), households.keySet().iterator().next());
        Assert.assertEquals(StatusCodes.OK, removeHouseholdResponse.status());
        
        // Get households
        getHouseholdsResponse = UserUtil.getHouseholds(loginResult.getUserId());
        Assert.assertEquals(StatusCodes.OK, getHouseholdsResponse.status());
        getHouseholdsResult = MessagesUtil.extractJSONFromResponse(getHouseholdsResponse, GetHouseholdsResultJSON.class);
        households = getHouseholdsResult.getHouseholds();
        Assert.assertTrue(households.isEmpty());
    }
}

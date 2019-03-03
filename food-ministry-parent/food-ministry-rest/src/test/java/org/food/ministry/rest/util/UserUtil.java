package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.user.json.AddHouseholdJSON;
import org.food.ministry.rest.user.json.GetHouseholdsJSON;
import org.food.ministry.rest.user.json.LoginUserJSON;
import org.food.ministry.rest.user.json.RegisterUserJSON;
import org.food.ministry.rest.user.json.RemoveHouseholdJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public class UserUtil {

    public static HttpResponse registerUser(String userName, String userEmailAddress, String userPassword) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        RegisterUserJSON registerUserJSON = new RegisterUserJSON(userName, userEmailAddress, userPassword);
        HttpRequest request = HttpRequest.PUT(ServerUtil.HOST_ROOT_URL + "register")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(registerUserJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }
    
    public static HttpResponse loginUser(String userEmailAddress, String userPassword) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        LoginUserJSON loginUserJSON = new LoginUserJSON(userEmailAddress, userPassword);
        HttpRequest request = HttpRequest.GET(ServerUtil.HOST_ROOT_URL + "login")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(loginUserJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for login");
        }

        return httpResponse;
    }
    
    public static HttpResponse addHousehold(long userId, String householdName) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        AddHouseholdJSON addHouseholdJSON = new AddHouseholdJSON(userId, householdName);
        HttpRequest request = HttpRequest.PUT(ServerUtil.HOST_ROOT_URL + "user/" + userId + "/households")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(addHouseholdJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for adding a household");
        }

        return httpResponse;
    }
    
    public static HttpResponse getHouseholds(long userId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        GetHouseholdsJSON addHouseholdJSON = new GetHouseholdsJSON(userId);
        HttpRequest request = HttpRequest.GET(ServerUtil.HOST_ROOT_URL + "user/" + userId + "/households")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(addHouseholdJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for getting households");
        }

        return httpResponse;
    }
    
    public static HttpResponse removeHousehold(long userId, long householdId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        RemoveHouseholdJSON removeHouseholdJSON = new RemoveHouseholdJSON(userId, householdId);
        HttpRequest request = HttpRequest.DELETE(ServerUtil.HOST_ROOT_URL + "user/" + userId + "/households")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(removeHouseholdJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for removing a household");
        }

        return httpResponse;
    }
}

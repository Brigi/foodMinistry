package org.food.ministry.rest;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.rest.user.json.LoginUserJSON;
import org.food.ministry.rest.user.json.RegisterUserJSON;
import org.food.ministry.rest.util.MessagesUtil;
import org.food.ministry.rest.util.ServerUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class TestUserEndpoint extends ServerManagerTestBase {

    private static final String USER_NAME = "MyName";
    private static final String USER_ADDRESS = "email@address.com";
    private static final String USER_PASSWORD = "MyPassword";

    @Test
    public void testSuccessfulRegisterUserAndLogin() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException {
        Assert.assertEquals(StatusCodes.CREATED, registerUser().status());
        Assert.assertEquals(StatusCodes.OK, loginUser().status());
    }

    @Test
    public void testFailedLogin() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse response = loginUser();
        Assert.assertEquals(StatusCodes.BAD_REQUEST, response.status());
        Assert.assertEquals(MessageFormat.format("Login for user {0} failed: ", USER_ADDRESS) + MessageFormat.format(UserDAO.NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, USER_ADDRESS),
                MessagesUtil.extractStringFromSource(response.entity().getDataBytes()));
    }

    @Test
    public void testDoubleRegistration() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException, TimeoutException {
        HttpResponse response = registerUser();
        Assert.assertEquals(StatusCodes.CREATED, response.status());
        response = registerUser();
        Assert.assertEquals(StatusCodes.BAD_REQUEST, response.status());
        Assert.assertEquals(MessageFormat.format("Registration of user {0} failed: ", USER_NAME) + MessageFormat.format(UserDAO.USER_WITH_EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, USER_ADDRESS),
                MessagesUtil.extractStringFromSource(response.entity().getDataBytes()));
    }

    private HttpResponse registerUser() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        RegisterUserJSON registerUserJSON = new RegisterUserJSON(USER_NAME, USER_ADDRESS, USER_PASSWORD);
        HttpRequest request = HttpRequest.PUT(ServerUtil.HOST_ROOT_URL + "user")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(registerUserJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }

    private HttpResponse loginUser() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        LoginUserJSON loginUserJSON = new LoginUserJSON(USER_ADDRESS, USER_PASSWORD);
        HttpRequest request = HttpRequest.GET(ServerUtil.HOST_ROOT_URL + "user")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(loginUserJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for login");
        }

        return httpResponse;
    }
}

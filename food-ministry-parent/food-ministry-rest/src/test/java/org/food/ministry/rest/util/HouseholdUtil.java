package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.household.json.GetFoodInventoryJSON;
import org.food.ministry.rest.household.json.GetIngredientsPoolJSON;
import org.food.ministry.rest.household.json.GetRecipesPoolJSON;
import org.food.ministry.rest.household.json.GetShoppingListJSON;
import org.food.ministry.rest.json.BaseRequestJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public class HouseholdUtil {

    public static final String BASE_URL = ServerUtil.HOST_ROOT_URL + "user/household/{0}";
    
    public static HttpResponse getFoodInventory(long userId, long householdId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendRequest(new GetFoodInventoryJSON(userId, householdId), "foodinventory");
    }
    
    public static HttpResponse getIngredientsPool(long userId, long householdId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendRequest(new GetIngredientsPoolJSON(userId, householdId), "ingredientspool");
    }
    
    public static HttpResponse getRecipesPool(long userId, long householdId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendRequest(new GetRecipesPoolJSON(userId, householdId), "recipespool");
    }
    
    public static HttpResponse getShoppingList(long userId, long householdId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendRequest(new GetShoppingListJSON(userId, householdId), "shoppinglist");
    }
    
    private static HttpResponse sendRequest(BaseRequestJSON requestJSON, String url) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        HttpRequest request = HttpRequest.GET(MessageFormat.format(BASE_URL, url))
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(requestJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }
}

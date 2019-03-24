package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.foodinventory.json.AddIngredientJSON;
import org.food.ministry.rest.foodinventory.json.GetIngredientsJSON;
import org.food.ministry.rest.json.BaseRequestJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public class FoodInventoryUtil {

    public static final String BASE_URL = ServerUtil.HOST_ROOT_URL + "user/foodinventory";
    
    public static HttpResponse addIngredient(long userId, long foodInventoryId, long ingredientId, float ingredientAmount) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendPutRequest(new AddIngredientJSON(userId, foodInventoryId, ingredientId, ingredientAmount));
    }
    
    public static HttpResponse getIngredients(long userId, long foodInventoryId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendGetRequest(new GetIngredientsJSON(userId, foodInventoryId));
    }
    
    private static HttpResponse sendPutRequest(BaseRequestJSON requestJSON) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        HttpRequest request = HttpRequest.PUT(BASE_URL)
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(requestJSON).getBytes("UTF-8")));
        return sendRequest(request);
    }
    
    private static HttpResponse sendGetRequest(BaseRequestJSON requestJSON) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        HttpRequest request = HttpRequest.GET(BASE_URL)
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(requestJSON).getBytes("UTF-8")));
        return sendRequest(request);
    }
    
    private static HttpResponse sendRequest(HttpRequest request) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }
}

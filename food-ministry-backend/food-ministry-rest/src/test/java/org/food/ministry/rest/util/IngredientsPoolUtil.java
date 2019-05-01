package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.model.Unit;
import org.food.ministry.rest.ingredientspool.json.AddIngredientJSON;
import org.food.ministry.rest.ingredientspool.json.GetIngredientsJSON;
import org.food.ministry.rest.json.BaseRequestJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public class IngredientsPoolUtil {

    public static final String BASE_URL = ServerUtil.HOST_ROOT_URL + "user/ingredientspool";
    
    public static HttpResponse addIngredient(long userId, long ingredientsPoolId, String ingredientName, Unit ingredientUnit, boolean isIngredientBasic) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendPutRequest(new AddIngredientJSON(userId, ingredientsPoolId, ingredientName, ingredientUnit, isIngredientBasic));
    }
    
    public static HttpResponse getIngredients(long userId, long ingredientsPoolId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendGetRequest(new GetIngredientsJSON(userId, ingredientsPoolId));
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

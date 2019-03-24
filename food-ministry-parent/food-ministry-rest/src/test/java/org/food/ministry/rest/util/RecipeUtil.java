package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.json.BaseRequestJSON;
import org.food.ministry.rest.recipe.json.GetRecipeJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public class RecipeUtil {

    public static final String BASE_URL = ServerUtil.HOST_ROOT_URL + "user/recipe";
    
    public static HttpResponse getRecipe(long userId, long recipeId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        return sendRequest(new GetRecipeJSON(userId, recipeId));
    }
    
    private static HttpResponse sendRequest(BaseRequestJSON requestJSON) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        HttpRequest request = HttpRequest.GET(BASE_URL)
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(requestJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }
}

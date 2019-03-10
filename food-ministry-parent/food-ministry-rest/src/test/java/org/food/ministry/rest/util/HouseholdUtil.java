package org.food.ministry.rest.util;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.household.json.GetFoodInventoryJSON;

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
        GetFoodInventoryJSON getFoodInventoryJSON = new GetFoodInventoryJSON(userId, householdId);
        HttpRequest request = HttpRequest.GET(MessageFormat.format(BASE_URL, "foodinventory"))
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(getFoodInventoryJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        HttpResponse httpResponse = response.toCompletableFuture().get();
        if(httpResponse == null) {
            fail("No response recieved for registering");
        }

        return httpResponse;
    }
}

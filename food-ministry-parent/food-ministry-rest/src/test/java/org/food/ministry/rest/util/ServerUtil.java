package org.food.ministry.rest.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.FoodMinistryInMemoryLauncher;
import org.food.ministry.rest.shutdown.json.ShutdownJSON;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;

public class ServerUtil {
    
    public static String HOST_ADDRESS = "localhost";
    public static int HOST_PORT = 8080;
    public static String HOST_ROOT_URL = "http://" + HOST_ADDRESS + ":" + HOST_PORT + "/";

    public static Thread startServer() throws InterruptedException {
        Thread thread = new Thread(() -> new FoodMinistryInMemoryLauncher().runServer(HOST_ADDRESS, HOST_PORT));
        thread.start();
        // Give the server some startup time for setting up the endpoints
        Thread.sleep(4000);
        
        return thread;
    }
    
    public static void stopServer() throws UnsupportedEncodingException, JsonProcessingException, InterruptedException, ExecutionException {
        ShutdownJSON shutdownJSON = new ShutdownJSON("Testcase shutdown");
        HttpRequest request = HttpRequest.GET(HOST_ROOT_URL + "shutdown").withEntity(
                HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(shutdownJSON).getBytes("UTF-8")));
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        try {
            HttpResponse httpResponse = response.toCompletableFuture().join();            
            Assert.assertEquals(MessageFormat.format("No response or OK response expected, but got: {0}", httpResponse.toString()), StatusCodes.OK, httpResponse.status());
        } catch(CompletionException e) {
            // Expected timeout -> Go on
        }
    }
}

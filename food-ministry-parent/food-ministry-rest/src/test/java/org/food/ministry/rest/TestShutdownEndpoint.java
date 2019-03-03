package org.food.ministry.rest;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.shutdown.json.ShutdownJSON;
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

public class TestShutdownEndpoint {

    @Test
    public void testShutdown() throws InterruptedException, ExecutionException, JsonProcessingException, UnsupportedEncodingException {
        Thread serverThread = ServerUtil.startServer();
        ShutdownJSON shutdownJSON = new ShutdownJSON("Test Shutdown");
        HttpRequest request = HttpRequest.GET(ServerUtil.HOST_ROOT_URL + "shutdown").withEntity(
                HttpEntities.create(ContentTypes.APPLICATION_JSON, new ObjectMapper().writeValueAsString(shutdownJSON).getBytes("UTF-8")));
        System.out.println(request.toString());
        CompletionStage<HttpResponse> response = Http.get(ActorSystem.create()).singleRequest(request);
        try {
            HttpResponse httpResponse = response.toCompletableFuture().join();
            fail(MessageFormat.format("No response expected, but got: {0}", httpResponse.toString()));
        } catch(CompletionException e) {
            // Expected timeout -> Go on
        }
        Assert.assertFalse(serverThread.isAlive());
    }
}

package org.food.ministry.rest.util;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;

public class MessagesUtil {

    public static <T> T extractJSONFromResponse(HttpResponse response, Class<T> jsonClass) throws InterruptedException, ExecutionException, TimeoutException {
        Materializer materializer = ActorMaterializer.create(ActorSystem.create());
        CompletionStage<T> errorJSON = Jackson.<T>unmarshaller(jsonClass).unmarshal(response.entity(), materializer);
        return errorJSON.toCompletableFuture().get(100, TimeUnit.MILLISECONDS);
    }
}

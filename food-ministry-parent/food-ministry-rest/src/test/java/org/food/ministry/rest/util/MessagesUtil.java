package org.food.ministry.rest.util;

import java.nio.charset.Charset;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;

public class MessagesUtil {

    public static String extractStringFromSource(Source<ByteString, Object> source) throws InterruptedException, ExecutionException, TimeoutException {
        final Sink<ByteString, CompletionStage<String>> sink = Sink.<String, ByteString> fold("", (aggr, next) -> aggr + next.decodeString(Charset.defaultCharset()));
        final RunnableGraph<CompletionStage<String>> runnable = source.toMat(sink, Keep.right());
        Materializer materializer = ActorMaterializer.create(ActorSystem.create());
        final CompletionStage<String> message = runnable.run(materializer);
        
        return message.toCompletableFuture().get(100, TimeUnit.MILLISECONDS);
    }
}

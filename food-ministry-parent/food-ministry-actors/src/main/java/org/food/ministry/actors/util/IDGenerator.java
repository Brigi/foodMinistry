package org.food.ministry.actors.util;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.TimeUnit;

import org.food.ministry.actors.factory.messages.NextIDMessage;
import org.food.ministry.actors.factory.messages.NextIDResultMessage;

import akka.actor.ActorRef;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class IDGenerator {

    private static ActorRef generatorActor;
    
    public static void initializeGeneratorActor(ActorRef generatorActor) {
        IDGenerator.generatorActor = generatorActor;
    }
    
    public static int getNextID() {
        Future<Object> nextIdFuture = ask(generatorActor, new NextIDMessage(), 150);
        NextIDResultMessage result;
        try {
            result = (NextIDResultMessage) Await.result(nextIdFuture, Duration.create(150, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            return 0;
        }
        return result.getNextId();
    }
}

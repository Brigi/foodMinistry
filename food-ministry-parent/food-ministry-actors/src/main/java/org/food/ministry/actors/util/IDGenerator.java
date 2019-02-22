package org.food.ministry.actors.util;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.TimeUnit;

import org.food.ministry.actors.factory.IDGeneratorActor;
import org.food.ministry.actors.factory.messages.UniqueIDMessage;
import org.food.ministry.actors.factory.messages.UniqueIDResultMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * This class simplifies the retrieving of IDs for messages by storing a
 * reference to a {@link IDGeneratorActor}, which gets asked synchronously for
 * unique IDs.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public final class IDGenerator {

    private IDGenerator() {
        /* No constructor needed as this a static class only */}

    /**
     * A reference to the {@link IDGeneratorActor}
     */
    private static ActorRef generatorActor;

    /**
     * Initializes the actor for a unique ID generation.
     * 
     * @param actorSystem The actor system in which the ID generator should be
     *            generated.
     */
    public static void initializeGeneratorActor(ActorSystem actorSystem) {
        generatorActor = actorSystem.actorOf(IDGeneratorActor.props(), "generator-actor");
    }

    /**
     * Gets the a unique ID from the actor for unique ID generation
     * 
     * @return A unique ID
     */
    public static long getRandomID() {
        if(generatorActor == null) {
            return 0;
        }
        Future<Object> nextIdFuture = ask(generatorActor, new UniqueIDMessage(), 150);
        UniqueIDResultMessage result;
        try {
            result = (UniqueIDResultMessage) Await.result(nextIdFuture, Duration.create(150, TimeUnit.MILLISECONDS));
        } catch(Exception e) {
            return 0;
        }
        return result.getNextId();
    }
}

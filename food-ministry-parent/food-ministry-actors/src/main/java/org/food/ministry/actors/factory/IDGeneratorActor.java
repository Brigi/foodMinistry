package org.food.ministry.actors.factory;

import org.food.ministry.actors.factory.messages.UniqueIDMessage;
import org.food.ministry.actors.factory.messages.UniqueIDResultMessage;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor provides unique IDs for messages.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class IDGeneratorActor extends AbstractActor {

    /**
     * The current free ID
     */
    private int currentID = 0;
    
    /**
     * Gets the property to create an actor of this class
     * @return The property for creating an actor of this class
     */
    public static Props props() {
        return Props.create(IDGeneratorActor.class, IDGeneratorActor::new);
    }
    
    /**
     * Accepts a {@link UniqueIDMessage} and returns a {@link UniqueIDResultMessage} with the requested ID in it
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(UniqueIDMessage.class, this::getNextID);
        
        return receiveBuilder.build();
    }

    /**
     * Retrieves the next free ID and sends it back to the requester via a {@link UniqueIDResultMessage}
     * @param message The {@link UniqueIDMessage} of the requester
     */
    private synchronized void getNextID(UniqueIDMessage message) {
        currentID++;
        getSender().tell(new UniqueIDResultMessage(currentID), getSelf());
    }
}

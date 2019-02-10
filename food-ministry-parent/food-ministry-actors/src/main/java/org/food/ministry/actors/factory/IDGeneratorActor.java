package org.food.ministry.actors.factory;

import org.food.ministry.actors.factory.messages.NextIDMessage;
import org.food.ministry.actors.factory.messages.NextIDResultMessage;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class IDGeneratorActor extends AbstractActor {

    private int currentID = 0;
    
    public static Props props() {
        return Props.create(IDGeneratorActor.class, IDGeneratorActor::new);
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(NextIDMessage.class, this::getNextID);
        
        return receiveBuilder.build();
    }

    private synchronized void getNextID(NextIDMessage message) {
        currentID++;
        getSender().tell(new NextIDResultMessage(currentID), getSelf());
    }
}

package org.food.ministry.actors.user;

import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.SubscribeMessage;
import org.food.ministry.actors.util.IDGenerator;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class SubscriptionActor extends AbstractActor {

    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(SubscriptionActor.class, SubscriptionActor::new);
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(SubscribeMessage.class, this::subscribe);
        
        return receiveBuilder.build();
    }
    
    private void subscribe(SubscribeMessage subscriptionMessage) {
        LOGGER.info("Subscribing in user {}", subscriptionMessage.getUsername());
        try {
            // Do subscription
            getSender().tell(new LoginResultMessage(IDGenerator.getNextID(), subscriptionMessage.getId(), true, "No Error."), getSelf());
        } catch(Exception e) {
            getSender().tell(new LoginResultMessage(IDGenerator.getNextID(), subscriptionMessage.getId(), false, "Login failed: " + e.getMessage()), getSelf());
        }
    }

}

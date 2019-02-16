package org.food.ministry.actors.user;

import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.SubscribeMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles a subscription request of a user.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class SubscriptionActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    /**
     * Gets the property to create an actor of this class
     * @return The property for creating an actor of this class
     */
    public static Props props() {
        return Props.create(SubscriptionActor.class, SubscriptionActor::new);
    }

    /**
     * Accepts a {@link SubscribeMessage} and tries to process the subscription of the user with the given information from the message. 
     * Afterwards a {@link SubscribeResultMessage} is send back to the requesting actor containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(SubscribeMessage.class, this::subscribe);
        
        return receiveBuilder.build();
    }
    
    /**
     * 
     * @param subscriptionMessage
     */
    private void subscribe(SubscribeMessage subscriptionMessage) {
        LOGGER.info("Subscribing in user {}", subscriptionMessage.getUsername());
        try {
            // Do subscription
            getSender().tell(new LoginResultMessage(IDGenerator.getUniqueID(), subscriptionMessage.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
        } catch(Exception e) {
            getSender().tell(new LoginResultMessage(IDGenerator.getUniqueID(), subscriptionMessage.getId(), false, "Login failed: " + e.getMessage()), getSelf());
        }
    }

}

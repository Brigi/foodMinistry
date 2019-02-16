package org.food.ministry.actors.user;

import org.food.ministry.actors.user.messages.DelegateMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.SubscribeMessage;
import org.food.ministry.actors.util.IDGenerator;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for user handling like login and subscription.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class UserActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for login handling
     */
    private final ActorRef loginChild = getContext().actorOf(Props.create(LoginActor.class), "loginActor");
    /**
     * The actor child for subscription handling
     */
    private final ActorRef subscriptionChild = getContext().actorOf(Props.create(SubscriptionActor.class), "subscriptionActor");
    
    /**
     * Gets the property to create an actor of this class
     * @return The property for creating an actor of this class
     */
    public static Props props() {
        return Props.create(UserActor.class, UserActor::new);
    }

    /**
     * Accepts {@link LoginMessage} and {@link SubscribeMessage}s and forwards those requests to the corresponding child actors
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::delegateToLoginActor);
        receiveBuilder.match(SubscribeMessage.class, this::delegateToSubscriptionActor);
        
        return receiveBuilder.build();
    }
    
    /**
     * Forwards the {@link LoginMessage} to the child {@link LoginActor} for further processing
     * @param loginMessage The message from the user to attempt a login
     */
    private void delegateToLoginActor(LoginMessage loginMessage) {
        LOGGER.info("Login in user with login message {}", loginMessage.getId());
        loginChild.forward(loginMessage, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getUniqueID(), loginMessage.getId()), getSelf());
    }
    
    /**
     * Forwards the {@link SubscribeMessage} to the child {@link SubscriptionActor} for further processing
     * @param subscribeMessage The message from the user to attempt a subscription
     */
    private void delegateToSubscriptionActor(SubscribeMessage subscribeMessage) {
        LOGGER.info("Subscribing user with subscribe message {}", subscribeMessage.getId());
        subscriptionChild.forward(subscribeMessage, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getUniqueID(), subscribeMessage.getId()), getSelf());        
    }
}

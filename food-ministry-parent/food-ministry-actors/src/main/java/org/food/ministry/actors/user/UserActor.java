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

public class UserActor extends AbstractActor {

    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef loginChild = getContext().actorOf(Props.create(LoginActor.class), "loginActor");
    private final ActorRef subscriptionChild = getContext().actorOf(Props.create(SubscriptionActor.class), "subscriptionActor");
    
    public static Props props() {
        return Props.create(UserActor.class, UserActor::new);
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::delegateToLoginActor);
        receiveBuilder.match(SubscribeMessage.class, this::delegateToSubscriptionActor);
        
        return receiveBuilder.build();
    }
    
    private void delegateToLoginActor(LoginMessage loginMessage) {
        LOGGER.info("Login in user with login message {}", loginMessage.getId());
        loginChild.forward(loginMessage, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getNextID(), loginMessage.getId()), getSelf());
    }
    
    private void delegateToSubscriptionActor(SubscribeMessage subscribeMessage) {
        LOGGER.info("Subscribing user with subscribe message {}", subscribeMessage.getId());
        subscriptionChild.forward(subscribeMessage, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getNextID(), subscribeMessage.getId()), getSelf());        
    }
}

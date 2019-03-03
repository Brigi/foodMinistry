package org.food.ministry.rest.actors;

import java.util.HashMap;
import java.util.Map;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IRequestMessage;
import org.food.ministry.actors.messages.IResultMessage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class SuppressDelegateMessageActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    private final Map<Long, ActorRef> requesters = new HashMap<>();
    
    private final ActorRef actorToForward;
    
    public SuppressDelegateMessageActor(ActorRef actorToForward) {
        this.actorToForward = actorToForward;
    }

    public static Props props(ActorRef actorToForward) {
        return Props.create(SuppressDelegateMessageActor.class, () -> new SuppressDelegateMessageActor(actorToForward));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(IRequestMessage.class, this::forwardToActor);
        receiveBuilder.match(DelegateMessage.class, this::acceptDelegateMessage);
        receiveBuilder.match(IResultMessage.class, this::acceptResultMessage);

        return receiveBuilder.build();
    }
    
    private void forwardToActor(IRequestMessage message) {
        LOGGER.info("Request ID: {}", message.getId());
        requesters.put(message.getId(), getSender());
        LOGGER.info("Forwarding request message to {}", actorToForward.toString());
        actorToForward.tell(message, getSelf());
    }
    
    private void acceptDelegateMessage(DelegateMessage message) {
        LOGGER.info("Suppressing delegate message");
    }
    
    private void acceptResultMessage(IResultMessage message) {
        LOGGER.info("Result ID: {}, Origin Request ID: {}", message.getId(), message.getOriginId());
        ActorRef requester = requesters.remove(message.getOriginId());
        LOGGER.info("Sender: {}, Requester: {}", getSender(), requester);
        LOGGER.info("Result message from {} sending back to {}", getSender().toString(), requester.toString());
        requester.tell(message, actorToForward);
    }
    
}

package org.food.ministry.actors.user;

import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.util.IDGenerator;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class LoginActor extends AbstractActor {

    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(LoginActor.class, LoginActor::new);
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::doLogin);
        
        return receiveBuilder.build();
    }
    
    private void doLogin(LoginMessage loginMessage) {
        LOGGER.info("Logging in user {}", loginMessage.getUsername());
        try {
            // Do login
            getSender().tell(new LoginResultMessage(IDGenerator.getNextID(), loginMessage.getId(), true, "No Error."), getSelf());
        } catch(Exception e) {
            getSender().tell(new LoginResultMessage(IDGenerator.getNextID(), loginMessage.getId(), false, "Login failed: " + e.getMessage()), getSelf());
        }
    }

}

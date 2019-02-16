package org.food.ministry.actors.user;

import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.util.IDGenerator;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles a login request of a user.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class LoginActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    /**
     * Gets the property to create an actor of this class
     * @return The property for creating an actor of this class
     */
    public static Props props() {
        return Props.create(LoginActor.class, LoginActor::new);
    }
    
    /**
     * Accepts a {@link LoginMessage} and tries to process a login the user with the given information from the message. 
     * Afterwards a {@link LoginResultMessage} is send back to the requesting actor containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::doLogin);
        
        return receiveBuilder.build();
    }
    
    /**
     * 
     * @param loginMessage
     */
    private void doLogin(LoginMessage loginMessage) {
        LOGGER.info("Logging in user {}", loginMessage.getUsername());
        try {
            // Do login
            getSender().tell(new LoginResultMessage(IDGenerator.getUniqueID(), loginMessage.getId(), true, "No Error."), getSelf());
        } catch(Exception e) {
            getSender().tell(new LoginResultMessage(IDGenerator.getUniqueID(), loginMessage.getId(), false, "Login failed: " + e.getMessage()), getSelf());
        }
    }

}

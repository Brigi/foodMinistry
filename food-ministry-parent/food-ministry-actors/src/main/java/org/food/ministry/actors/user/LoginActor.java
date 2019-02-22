package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.User;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles a login request of a user.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class LoginActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for users
     */
    private final UserDAO userDao;

    /**
     * Constructor setting the data access object for users
     * 
     * @param userDao The data access object for users
     */
    public LoginActor(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param userDao The data access object for users
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao) {
        return Props.create(LoginActor.class, () -> new LoginActor(userDao));
    }

    /**
     * Accepts a {@link LoginMessage} and tries to process a login the user with the
     * given information from the message. Afterwards a {@link LoginResultMessage}
     * is send back to the requesting actor containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::doLogin);

        return receiveBuilder.build();
    }

    /**
     * Tries to login the user contained in provided the login message with the
     * contained credentials
     * 
     * @param message The login message containing all needed information for
     *            logging in a user
     */
    private void doLogin(LoginMessage message) {
        String userName = message.getUsername();
        LOGGER.info("Logging in user {}", userName);
        try {
            String emailAddress = message.getEmailAddress();
            String password = message.getPassword();
            LOGGER.info("Retrieving user data for user {}", userName);
            User user = userDao.getUser(emailAddress);
            if(user.getPassword().equals(password)) {
                getSender().tell(new LoginResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
                LOGGER.info("Successfully logged in user {}", userName);
            } else {
                final String errorMessage = MessageFormat.format("Login for user {0} failed: {1}", userName, Constants.WRONG_CREDENTIALS_MESSAGE);
                LOGGER.info(errorMessage);
                getSender().tell(new LoginResultMessage(IDGenerator.getRandomID(), message.getId(), false, Constants.WRONG_CREDENTIALS_MESSAGE), getSelf());
            }
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Login for user {0} failed: {1}", userName, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new LoginResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }

}

package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.user.messages.RegisterResultMessage;
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
 * This actor handles a registration request of a user.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class RegistrationActor extends AbstractActor {

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
     * @param userDao
     *            The data access object for users
     */
    public RegistrationActor(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao) {
        return Props.create(RegistrationActor.class, () -> new RegistrationActor(userDao));
    }

    /**
     * Accepts a {@link RegisterMessage} and tries to process the registration of
     * the user with the given information from the message. Afterwards a
     * {@link RegisterResultMessage} is send back to the requesting actor containing
     * the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(RegisterMessage.class, this::register);

        return receiveBuilder.build();
    }

    /**
     * Tries to register the user contained in the provided register message
     * 
     * @param message
     *            The register message containing all needed information for
     *            registering a user
     */
    private void register(RegisterMessage message) {
        String emailAddress = message.getEmailAddress();
        String userName = message.getUsername();
        String password = message.getPassword();
        LOGGER.info("Registering user {}", userName);
        try {
            if(userDao.doesEmailAddressExist(emailAddress)) {
                LOGGER.info("Registration of user {} failed due to already existing email address", userName);
                getSender().tell(new RegisterResultMessage(IDGenerator.getRandomID(), message.getId(), false, Constants.EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE), getSelf());
            }
            long id = UtilFunctions.generateUniqueId(userDao, LOGGER);
            userDao.save(new User(id, emailAddress, userName, password));
            getSender().tell(new RegisterResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully registered user {}", userName);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Registration of user {0} failed: {1}", userName, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new RegisterResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }

}

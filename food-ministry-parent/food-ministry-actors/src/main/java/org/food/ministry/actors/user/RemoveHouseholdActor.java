package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.user.messages.RemoveHouseholdMessage;
import org.food.ministry.actors.user.messages.RemoveHouseholdResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.Household;
import org.food.ministry.model.User;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for removing households from a user.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class RemoveHouseholdActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for users
     */
    private UserDAO userDao;
    /**
     * The data access object for households
     */
    private HouseholdDAO householdDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param userDao The data access object for users
     * @param householdDao The data access object for households
     */
    public RemoveHouseholdActor(UserDAO userDao, HouseholdDAO householdDao) {
        this.userDao = userDao;
        this.householdDao = householdDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param userDao The data access object for users
     * @param householdDao The data access object for households
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao, HouseholdDAO householdDao) {
        return Props.create(RemoveHouseholdActor.class, () -> new RemoveHouseholdActor(userDao, householdDao));
    }

    /**
     * Accepts a {@link RemoveHouseholdMessage} and tries to remove a household from
     * the user with the given information from the message. Afterwards a
     * {@link RemoveHouseholdResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(RemoveHouseholdMessage.class, this::deleteHousehold);

        return receiveBuilder.build();
    }

    /**
     * Tries to remove a household from the user contained in provided the message
     * with the contained user id
     * 
     * @param message The message containing all needed information for removing a
     *            household for a user
     */
    private void deleteHousehold(RemoveHouseholdMessage message) {
        long userId = message.getUserId();
        long householdId = message.getHouseholdId();
        LOGGER.info("Removing household with id {} for user with id {}", householdId, userId);
        try {
            User user = userDao.get(userId);
            Household household = householdDao.get(householdId);
            user.removeHousehold(household);
            userDao.update(user);
            LOGGER.info("Successfully removed household {} from user with id {}", householdId, userId);
            if(userDao.isHouseholdUnreferenced(household)) {
                householdDao.delete(household);
                LOGGER.info("Successfully deleted household with id {} as no user references it anymore", householdId, userId);
            }
            RemoveHouseholdResultMessage resultMessage = new RemoveHouseholdResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE);
            getSender().tell(resultMessage, getSelf());
            LOGGER.info("Successfully added a household for user with id {}", userId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Removing household with id {0} for user with id {1} failed: {2}", householdId, userId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new RemoveHouseholdResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.actors.user.messages.GetHouseholdsMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.Household;
import org.food.ministry.model.User;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class GetHouseholdsActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    private UserDAO userDao;

    public GetHouseholdsActor(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao) {
        return Props.create(GetHouseholdsActor.class, () -> new GetHouseholdsActor(userDao));
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetHouseholdsMessage.class, this::getHouseholds);

        return receiveBuilder.build();
    }

    /**
     * Tries to get all households for the user contained in provided the message
     * with the contained user id
     * 
     * @param message
     *            The message containing all needed information for getting all
     *            households for a user
     */
    private void getHouseholds(GetHouseholdsMessage message) {
        long userId = message.getUserId();
        LOGGER.info("Getting households for user with id {}", userId);
        try {
            User user = userDao.get(userId);
            Map<Long, String> households = user.getHouseholds().parallelStream().collect(Collectors.toMap(Household::getId, Household::getName));
            GetHouseholdsResultMessage resultMessage = new GetHouseholdsResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, households);
            getSender().tell(resultMessage, getSelf());
            LOGGER.info("Successfully got households for user with id {}", userId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting households for user id {0} failed: {1}", userId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetHouseholdsResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, new HashMap<>()), getSelf());
        }
    }
}

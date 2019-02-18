package org.food.ministry.actors.user;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.user.messages.AddHouseholdMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for user handling like login and
 * subscription.
 * 
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
    private final ActorRef loginChild;
    /**
     * The actor child for subscription handling
     */
    private final ActorRef registrationChild;
    /**
     * The actor child getting households
     */
    private final ActorRef getHouseholdsChild;
    /**
     * The actor child adding a household
     */
    private final ActorRef addHouseholdsChild;

    public UserActor(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao, IngredientsPoolDAO ingredientsPoolDao) {
        loginChild = getContext().actorOf(LoginActor.props(userDao), "loginActor");
        registrationChild = getContext().actorOf(RegistrationActor.props(userDao), "registrationActor");
        getHouseholdsChild = getContext().actorOf(GetHouseholdsActor.props(userDao), "getHouseholdsActor");
        addHouseholdsChild = getContext().actorOf(AddHouseholdActor.props(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao), "addHouseholdActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao, IngredientsPoolDAO ingredientsPoolDao) {
        return Props.create(UserActor.class, () -> new UserActor(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao));
    }

    /**
     * Accepts {@link LoginMessage} and {@link RegisterMessage}s and forwards those
     * requests to the corresponding child actors
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(LoginMessage.class, this::delegateToLoginActor);
        receiveBuilder.match(RegisterMessage.class, this::delegateToRegistrationActor);
        receiveBuilder.match(GetHouseholdsMessage.class, this::delegateToGetHouseholdsActor);
        receiveBuilder.match(AddHouseholdMessage.class, this::delegateToAddHouseholdActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link LoginMessage} to the child {@link LoginActor} for further
     * processing
     * 
     * @param message
     *            The message from the user to attempt a login
     */
    private void delegateToLoginActor(LoginMessage message) {
        LOGGER.info("Login in user with message {}", message.getId());
        loginChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link RegisterMessage} to the child {@link RegistrationActor}
     * for further processing
     * 
     * @param message
     *            The message from the user to attempt a subscription
     */
    private void delegateToRegistrationActor(RegisterMessage message) {
        LOGGER.info("Registering user with message {}", message.getId());
        registrationChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link GetHouseholdsMessage} to the child {@link GetHouseholdsActor}
     * for further processing
     * 
     * @param message
     *            The message from the user to get all households
     */
    private void delegateToGetHouseholdsActor(GetHouseholdsMessage message) {
        LOGGER.info("Getting households with message {}", message.getId());
        getHouseholdsChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
    
    /**
     * Forwards the {@link AddHouseholdsMessage} to the child {@link AddHouseholdActor}
     * for further processing
     * 
     * @param message
     *            The message from the user to add a household
     */
    private void delegateToAddHouseholdActor(AddHouseholdMessage message) {
        LOGGER.info("Adding a household with message {}", message.getId());
        addHouseholdsChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

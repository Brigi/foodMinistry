package org.food.ministry.actors.household;

import org.food.ministry.actors.household.messages.GetFoodInventoryMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolMessage;
import org.food.ministry.actors.household.messages.GetShoppingListMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.household.HouseholdDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for household handling like getting food
 * inventories, ingredients pools, recipes pools and shopping lists.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class HouseholdActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for recipes pool handling
     */
    private final ActorRef getRecipesPoolChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param householdDao The data access object for households
     */
    public HouseholdActor(HouseholdDAO householdDao) {
        getRecipesPoolChild = getContext().actorOf(GetRecipesPoolActor.props(householdDao), "getRecipesPoolActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param householdDao The data access object for households
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(HouseholdActor.class, () -> new HouseholdActor(householdDao));
    }

    /**
     * Accepts {@link GetFoodInventoryMessage}, {@link GetIngredientsPoolMessage},
     * {@link GetRecipesPoolMessage}, {@link GetShoppingListMessage}s and forwards
     * those requests to the corresponding child actors
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetFoodInventoryMessage.class, this::delegateToGetFoodInventoryActor);
        receiveBuilder.match(GetIngredientsPoolMessage.class, this::delegateToGetIngredientsPoolActor);
        receiveBuilder.match(GetRecipesPoolMessage.class, this::delegateToGetRecipesPoolActor);
        receiveBuilder.match(GetShoppingListMessage.class, this::delegateToGetShoppingListActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetFoodInventoryMessage} to the child
     * {@link GetFoodInventoryActor} for further processing
     * 
     * @param message The message from the user to get a food inventory
     */
    private void delegateToGetFoodInventoryActor(GetFoodInventoryMessage message) {
        LOGGER.info("Getting food inventory with message {}", message.getId());
        getRecipesPoolChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link GetIngredientsPoolMessage} to the child
     * {@link GetIngredientsPoolActor} for further processing
     * 
     * @param message The message from the user to get a ingredients pool
     */
    private void delegateToGetIngredientsPoolActor(GetIngredientsPoolMessage message) {
        LOGGER.info("Getting ingredients pool with message {}", message.getId());
        getRecipesPoolChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link GetRecipesPoolMessage} to the child
     * {@link GetRecipesPoolActor} for further processing
     * 
     * @param message The message from the user to get a recipes pool
     */
    private void delegateToGetRecipesPoolActor(GetRecipesPoolMessage message) {
        LOGGER.info("Getting recipes pool with message {}", message.getId());
        getRecipesPoolChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link GetShoppingListMessage} to the child
     * {@link GetShoppingListActor} for further processing
     * 
     * @param message The message from the user to get a shopping list
     */
    private void delegateToGetShoppingListActor(GetShoppingListMessage message) {
        LOGGER.info("Getting shopping list with message {}", message.getId());
        getRecipesPoolChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

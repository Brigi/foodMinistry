package org.food.ministry.actors.foodinventory;

import org.food.ministry.actors.foodinventory.messages.AddIngredientMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for food inventories
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class FoodInventoryActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for getting ingredients
     */
    private final ActorRef getIngredientsChild;
    
    /**
     * The actor child for adding ingredients
     */
    private final ActorRef addIngredientChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param foodInventoryDao The data access object for food inventories
     * @param ingredientDao The data access object for ingredients
     */
    public FoodInventoryActor(FoodInventoryDAO foodInventoryDao, IngredientDAO ingredientDao) {
        getIngredientsChild = getContext().actorOf(GetIngredientsActor.props(foodInventoryDao), "getIngredientsActor");
        addIngredientChild = getContext().actorOf(AddIngredientActor.props(foodInventoryDao, ingredientDao), "addIngredientActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param foodInventoryDao The data access object for food inventories
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(FoodInventoryDAO foodInventoryDao, IngredientDAO ingredientDao) {
        return Props.create(FoodInventoryActor.class, () -> new FoodInventoryActor(foodInventoryDao, ingredientDao));
    }

    /**
     * Accepts {@link GetIngredientsMessage}, {@link AddIngredientMessage},
     * {@link DeleteIngredientMessage}s and forwards those requests to the corresponding
     * child actors
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientsMessage.class, this::delegateToGetIngredientsActor);
        receiveBuilder.match(AddIngredientMessage.class, this::delegateToAddIngredientActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetIngredientsMessage} to the child {@link GetIngredientsActor}
     * for further processing
     * 
     * @param message The message from the user to get all ingredients
     */
    private void delegateToGetIngredientsActor(GetIngredientsMessage message) {
        LOGGER.info("Getting all ingredients with message {}", message.getId());
        getIngredientsChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link AddIngredientMessage} to the child {@link AddIngredientActor}
     * for further processing
     * 
     * @param message The message from the user to add/remove an ingredient
     */
    private void delegateToAddIngredientActor(AddIngredientMessage message) {
        LOGGER.info("Adding/Removing a ingredient with message {}", message.getId());
        addIngredientChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

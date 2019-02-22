package org.food.ministry.actors.ingredientspool;

import org.food.ministry.actors.ingredientspool.messages.AddIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.DeleteIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for ingredients pool handling like getting,
 * adding and deleting ingredients from ingredients pools.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class IngredientsPoolActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for getting ingredient
     */
    private final ActorRef getIngredientsChild;
    /**
     * The actor child for adding ingredients
     */
    private final ActorRef addIngredientChild;
    /**
     * The actor child for deleting ingredients
     */
    private final ActorRef deleteIngredientChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDao The data access object for ingredients
     */
    public IngredientsPoolActor(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        getIngredientsChild = getContext().actorOf(GetIngredientsActor.props(ingredientsPoolDao), "getIngredientsActor");
        addIngredientChild = getContext().actorOf(AddIngredientActor.props(ingredientsPoolDao, ingredientDao), "addIngredientActor");
        deleteIngredientChild = getContext().actorOf(DeleteIngredientActor.props(ingredientsPoolDao, ingredientDao), "deleteIngredientActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        return Props.create(IngredientsPoolActor.class, () -> new IngredientsPoolActor(ingredientsPoolDao, ingredientDao));
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
        receiveBuilder.match(DeleteIngredientMessage.class, this::delegateToDeleteIngredientsActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetIngredientsMessage} to the child {@link GetIngredientsActor}
     * for further processing
     * 
     * @param message The message from the user to get all ingredients
     */
    private void delegateToGetIngredientsActor(GetIngredientsMessage message) {
        LOGGER.info("Getting ingredients with message {}", message.getId());
        getIngredientsChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link AddIngredientMessage} to the child {@link AddIngredientActor} for
     * further processing
     * 
     * @param message The message from the user to add an ingredient
     */
    private void delegateToAddIngredientActor(AddIngredientMessage message) {
        LOGGER.info("Adding an ingredient with message {}", message.getId());
        addIngredientChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link DeleteIngredientMessage} to the child
     * {@link DeleteIngredientActor} for further processing
     * 
     * @param message The message from the user to delete an ingredient
     */
    private void delegateToDeleteIngredientsActor(DeleteIngredientMessage message) {
        LOGGER.info("Deleting an ingredient with message {}", message.getId());
        deleteIngredientChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

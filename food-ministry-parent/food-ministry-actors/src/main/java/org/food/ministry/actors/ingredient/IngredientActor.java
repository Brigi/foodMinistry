package org.food.ministry.actors.ingredient;

import org.food.ministry.actors.ingredient.messages.GetIngredientMessage;
import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for ingredients
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class IngredientActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for getting ingredient
     */
    private final ActorRef getIngredientChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param ingredientDao The data access object for ingredients
     */
    public IngredientActor(IngredientDAO ingredientDao) {
        getIngredientChild = getContext().actorOf(GetIngredientActor.props(ingredientDao), "getIngredientActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientDAO ingredientDao) {
        return Props.create(IngredientActor.class, () -> new IngredientActor(ingredientDao));
    }

    /**
     * Accepts a {@link GetIngredientMessage} and forwards the requests to the corresponding
     * child actor
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientMessage.class, this::delegateToGetIngredientActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetIngredientMessage} to the child {@link GetIngredientActor}
     * for further processing
     * 
     * @param message The message from the user to get an ingredient
     */
    private void delegateToGetIngredientActor(GetIngredientMessage message) {
        LOGGER.info("Getting an ingredient with message {}", message.getId());
        getIngredientChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

}

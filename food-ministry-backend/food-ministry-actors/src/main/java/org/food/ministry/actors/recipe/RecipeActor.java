package org.food.ministry.actors.recipe;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.recipe.messages.GetRecipeMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.recipe.RecipeDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for recipes
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class RecipeActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for getting recipes
     */
    private final ActorRef getRecipeChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param recipeDao The data access object for recipes
     */
    public RecipeActor(RecipeDAO recipeDao) {
        getRecipeChild = getContext().actorOf(GetRecipeActor.props(recipeDao), "getRecipeActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param recipeDao The data access object for recipes
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipeDAO recipeDao) {
        return Props.create(RecipeActor.class, () -> new RecipeActor(recipeDao));
    }

    /**
     * Accepts a {@link GetRecipeMessage} and forwards the request to the corresponding
     * child actor
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipeMessage.class, this::delegateToGetRecipeActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetRecipeMessage} to the child {@link GetRecipeActor}
     * for further processing
     * 
     * @param message The message from the user to get a recipe
     */
    private void delegateToGetRecipeActor(GetRecipeMessage message) {
        LOGGER.info("Getting a recipe with message {}", message.getId());
        getRecipeChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

}

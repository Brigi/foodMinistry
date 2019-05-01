package org.food.ministry.actors.recipespool;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.recipespool.messages.AddRecipeMessage;
import org.food.ministry.actors.recipespool.messages.DeleteRecipeMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles user requests for recipes pool handling like getting,
 * adding and deleting recipes from recipes pools.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class RecipesPoolActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The actor child for getting recipes
     */
    private final ActorRef getRecipesChild;
    /**
     * The actor child for adding recipes
     */
    private final ActorRef addRecipeChild;
    /**
     * The actor child for deleting recipes
     */
    private final ActorRef deleteRecipeChild;

    /**
     * Constructor initializing all child actors
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     * @param ingredientDao The data access object for ingredients
     */
    public RecipesPoolActor(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDao) {
        getRecipesChild = getContext().actorOf(GetRecipesActor.props(recipesPoolDao), "getRecipesActor");
        addRecipeChild = getContext().actorOf(AddRecipeActor.props(recipesPoolDao, recipeDao, ingredientDao), "addRecipeActor");
        deleteRecipeChild = getContext().actorOf(DeleteRecipeActor.props(recipesPoolDao, recipeDao), "deleteRecipeActor");
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDao) {
        return Props.create(RecipesPoolActor.class, () -> new RecipesPoolActor(recipesPoolDao, recipeDao, ingredientDao));
    }

    /**
     * Accepts {@link GetRecipesMessage}, {@link AddRecipeMessage},
     * {@link DeleteRecipeMessage}s and forwards those requests to the corresponding
     * child actors
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesMessage.class, this::delegateToGetRecipesActor);
        receiveBuilder.match(AddRecipeMessage.class, this::delegateToAddRecipeActor);
        receiveBuilder.match(DeleteRecipeMessage.class, this::delegateToDeleteRecipeActor);

        return receiveBuilder.build();
    }

    /**
     * Forwards the {@link GetRecipesMessage} to the child {@link GetRecipesActor}
     * for further processing
     * 
     * @param message The message from the user to get all recipes
     */
    private void delegateToGetRecipesActor(GetRecipesMessage message) {
        LOGGER.info("Getting recipes with message {}", message.getId());
        getRecipesChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link AddRecipeMessage} to the child {@link AddRecipeActor} for
     * further processing
     * 
     * @param message The message from the user to add a recipe
     */
    private void delegateToAddRecipeActor(AddRecipeMessage message) {
        LOGGER.info("Adding a recipe with message {}", message.getId());
        addRecipeChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }

    /**
     * Forwards the {@link DeleteRecipeMessage} to the child
     * {@link DeleteRecipeActor} for further processing
     * 
     * @param message The message from the user to delete a recipe
     */
    private void delegateToDeleteRecipeActor(DeleteRecipeMessage message) {
        LOGGER.info("Deleting a recipe with message {}", message.getId());
        deleteRecipeChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

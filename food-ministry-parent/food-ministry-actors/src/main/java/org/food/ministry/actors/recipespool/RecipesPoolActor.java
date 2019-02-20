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

public class RecipesPoolActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    /**
     * The actor child for recipes handling
     */
    private final ActorRef getRecipesChild;
    /**
     * The actor child for recipes handling
     */
    private final ActorRef addRecipeChild;
    /**
     * The actor child for recipes handling
     */
    private final ActorRef deleteRecipeChild;
    
    public RecipesPoolActor(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDao) {
        getRecipesChild = getContext().actorOf(GetRecipesActor.props(recipesPoolDao), "getRecipesActor");
        addRecipeChild = getContext().actorOf(AddRecipeActor.props(recipesPoolDao, recipeDao, ingredientDao), "addRecipeActor");
        deleteRecipeChild = getContext().actorOf(DeleteRecipeActor.props(recipesPoolDao, recipeDao), "deleteRecipeActor");
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDao) {
        return Props.create(RecipesPoolActor.class, () -> new RecipesPoolActor(recipesPoolDao, recipeDao, ingredientDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesMessage.class, this::delegateToGetRecipesActor);
        receiveBuilder.match(AddRecipeMessage.class, this::delegateToAddRecipeActor);
        receiveBuilder.match(DeleteRecipeMessage.class, this::delegateToGetRecipesActor);

        return receiveBuilder.build();
    }
    
    private void delegateToGetRecipesActor(GetRecipesMessage message) {
        LOGGER.info("Getting recipes with message {}", message.getId());
        getRecipesChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
    
    private void delegateToAddRecipeActor(AddRecipeMessage message) {
        LOGGER.info("Adding a recipe with message {}", message.getId());
        addRecipeChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
    
    private void delegateToGetRecipesActor(DeleteRecipeMessage message) {
        LOGGER.info("Deleting a recipe with message {}", message.getId());
        deleteRecipeChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

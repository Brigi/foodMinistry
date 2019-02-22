package org.food.ministry.actors.recipespool;

import java.text.MessageFormat;

import org.food.ministry.actors.recipespool.messages.DeleteRecipeMessage;
import org.food.ministry.actors.recipespool.messages.DeleteRecipeResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.RecipesPool;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for deleting recipes to a recipes pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class DeleteRecipeActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for recipes pools
     */
    private RecipesPoolDAO recipesPoolDao;
    /**
     * The data access object for recipes
     */
    private RecipeDAO recipeDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     */
    public DeleteRecipeActor(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao) {
        this.recipesPoolDao = recipesPoolDao;
        this.recipeDao = recipeDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao) {
        return Props.create(DeleteRecipeActor.class, () -> new DeleteRecipeActor(recipesPoolDao, recipeDao));
    }

    /**
     * Accepts a {@link DeleteRecipeMessage} and tries to delete a recipe from the
     * recipes pool with the given information from the message. Afterwards a
     * {@link DeleteRecipeResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(DeleteRecipeMessage.class, this::deleteRecipe);

        return receiveBuilder.build();
    }

    /**
     * Tries to delete a recipe from the recipes pool contained in provided the
     * message with the contained user id
     * 
     * @param message The message containing all needed information for deleting a
     *            recipe from a recipes pool
     */
    private void deleteRecipe(DeleteRecipeMessage message) {
        long recipesPoolId = message.getRecipesPoolId();
        long recipeId = message.getRecipeId();
        LOGGER.info("Deleting recipe with id {} from recipes pool with id {}", recipeId, recipesPoolId);
        try {
            RecipesPool recipesPool = recipesPoolDao.get(recipesPoolId);
            Recipe recipe = recipeDao.get(recipeId);
            recipesPool.removeRecipe(recipe);
            recipeDao.delete(recipe);
            recipesPoolDao.update(recipesPool);
            getSender().tell(new DeleteRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully deleted recipe with id {} from recipes pool with id {}", recipeId, recipesPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Deleting of recipe with {0} from recipes pool with id {1} failed: {2}", recipesPoolId, recipeId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new DeleteRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

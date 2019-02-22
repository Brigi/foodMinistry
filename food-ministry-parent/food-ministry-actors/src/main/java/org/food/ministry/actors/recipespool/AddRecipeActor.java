package org.food.ministry.actors.recipespool;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.food.ministry.actors.recipespool.messages.AddRecipeMessage;
import org.food.ministry.actors.recipespool.messages.AddRecipeResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.RecipesPool;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for adding recipes to a recipes pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddRecipeActor extends AbstractActor {

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
     * The data access object for ingredients pools
     */
    private IngredientDAO ingredientDAO;

    /**
     * Constructor setting the data access objects
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     * @param ingredientDAO The data access object for ingredients pools
     */
    public AddRecipeActor(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDAO) {
        this.recipesPoolDao = recipesPoolDao;
        this.recipeDao = recipeDao;
        this.ingredientDAO = ingredientDAO;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param recipesPoolDao The data access object for recipes pools
     * @param recipeDao The data access object for recipes
     * @param ingredientDAO The data access object for ingredients pools
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDAO) {
        return Props.create(AddRecipeActor.class, () -> new AddRecipeActor(recipesPoolDao, recipeDao, ingredientDAO));
    }

    /**
     * Accepts a {@link AddRecipeMessage} and tries to add a recipe to the recipes
     * pool with the given information from the message. Afterwards a
     * {@link AddRecipeResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(AddRecipeMessage.class, this::addRecipe);

        return receiveBuilder.build();
    }

    /**
     * Tries to add a recipe to the recipes pool contained in provided the message
     * with the contained user id
     * 
     * @param message The message containing all needed information for adding a
     *            recipe to a recipes pool
     */
    private void addRecipe(AddRecipeMessage message) {
        long recipesPoolId = message.getRecipesPoolId();
        LOGGER.info("Adding a recipe to recipes pool with id {}", recipesPoolId);
        try {
            RecipesPool recipesPool = recipesPoolDao.get(recipesPoolId);
            long recipeId = UtilFunctions.generateUniqueId(recipesPoolDao, LOGGER);
            Map<Ingredient, Float> ingredientsToAmountMap = new HashMap<>();
            for(Entry<Long, Float> ingredientIdToAmount: message.getIngredientIdsWithAmount().entrySet()) {
                ingredientsToAmountMap.put(ingredientDAO.get(ingredientIdToAmount.getKey()), ingredientIdToAmount.getValue());
            }
            Recipe newRecipe = new Recipe(recipeId, message.getRecipeName(), ingredientsToAmountMap, message.getRecipeDescription());
            recipesPool.addRecipe(newRecipe);
            recipeDao.save(newRecipe);
            recipesPoolDao.update(recipesPool);
            getSender().tell(new AddRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully added a recipe with id {} to recipes pool with id {}", recipeId, recipesPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Adding a recipe to recipes pool with id {0} failed: {1}", recipesPoolId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new AddRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

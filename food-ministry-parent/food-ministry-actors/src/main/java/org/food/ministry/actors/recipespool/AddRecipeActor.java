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

public class AddRecipeActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    private RecipesPoolDAO recipesPoolDao;
    private RecipeDAO recipeDao;
    private IngredientDAO ingredientDAO;
    
    public AddRecipeActor(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDAO) {
        this.recipesPoolDao = recipesPoolDao;
        this.recipeDao = recipeDao;
        this.ingredientDAO = ingredientDAO;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDAO) {
        return Props.create(AddRecipeActor.class, () -> new AddRecipeActor(recipesPoolDao, recipeDao, ingredientDAO));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(AddRecipeMessage.class, this::addRecipe);

        return receiveBuilder.build();
    }

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
            final String errorMessage = MessageFormat.format("Adding a recipe to recipes pool id {0} failed: {1}", recipesPoolId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new AddRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

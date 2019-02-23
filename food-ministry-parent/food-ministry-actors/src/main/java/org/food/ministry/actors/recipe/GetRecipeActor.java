package org.food.ministry.actors.recipe;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.food.ministry.actors.recipe.messages.GetRecipeMessage;
import org.food.ministry.actors.recipe.messages.GetRecipeResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.model.Recipe;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for getting recipes
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetRecipeActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for recipes
     */
    private RecipeDAO recipeDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param recipeDao The data access object for recipes
     */
    public GetRecipeActor(RecipeDAO recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param recipeDao The data access object for recipes
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipeDAO recipeDao) {
        return Props.create(GetRecipeActor.class, () -> new GetRecipeActor(recipeDao));
    }

    /**
     * Accepts a {@link GetRecipeMessage} and tries to get the recipe with the
     * given information from the message. Afterwards a
     * {@link GetRecipeResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipeMessage.class, this::getRecipe);

        return receiveBuilder.build();
    }

    /**
     * Tries to get the recipe contained in provided the message with the
     * contained recipe id
     * 
     * @param message The message containing all needed information for getting a
     *            recipe
     */
    private void getRecipe(GetRecipeMessage message) {
        long recipeId = message.getRecipeId();
        LOGGER.info("Getting recipe with id {}", recipeId);
        try {
            Recipe recipe = recipeDao.get(recipeId);
            Map<Long, Float> ingredientsWithAmountMap = recipe.getIngredientsWithQuantity().entrySet().parallelStream()
                    .collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
            getSender().tell(new GetRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, recipe.getName(), ingredientsWithAmountMap,
                    recipe.getDescription()), getSelf());
            LOGGER.info("Successfully got recipe with id {} ", recipeId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting recipe with id {0} failed: {1}", recipeId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetRecipeResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, "", new HashMap<>(), ""), getSelf());
        }
    }
}

package org.food.ministry.actors.ingredientspool;

import java.text.MessageFormat;

import org.food.ministry.actors.ingredientspool.messages.DeleteIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.DeleteIngredientResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.IngredientsPool;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for deleting ingredients to a ingredient pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class DeleteIngredientActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for ingredients pools
     */
    private IngredientsPoolDAO ingredientsPoolDao;
    /**
     * The data access object for ingredients
     */
    private IngredientDAO ingredientDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDao The data access object for ingredients
     */
    public DeleteIngredientActor(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        this.ingredientsPoolDao = ingredientsPoolDao;
        this.ingredientDao = ingredientDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        return Props.create(DeleteIngredientActor.class, () -> new DeleteIngredientActor(ingredientsPoolDao, ingredientDao));
    }

    /**
     * Accepts a {@link DeleteIngredientMessage} and tries to delete an ingredient from the
     * ingredients pool with the given information from the message. Afterwards a
     * {@link DeleteIngredientResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(DeleteIngredientMessage.class, this::deleteIngredinet);

        return receiveBuilder.build();
    }

    /**
     * Tries to delete an ingredient from the ingredients pool contained in provided the
     * message with the contained ingredients pool id
     * 
     * @param message The message containing all needed information for deleting an
     *            ingredient from a ingredients pool
     */
    private void deleteIngredinet(DeleteIngredientMessage message) {
        long ingredientsPoolId = message.getIngredientsPoolId();
        long ingredientId = message.getIngredientId();
        LOGGER.info("Deleting ingredient with id {} from ingredients pool with id {}", ingredientId, ingredientsPoolId);
        try {
            IngredientsPool ingredientsPool = ingredientsPoolDao.get(ingredientsPoolId);
            Ingredient ingredient = ingredientDao.get(ingredientId);
            ingredientsPool.removeIngredient(ingredient);
            ingredientDao.delete(ingredient);
            ingredientsPoolDao.update(ingredientsPool);
            getSender().tell(new DeleteIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully deleted ingredient with id {} from ingredients pool with id {}", ingredientId, ingredientsPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Deleting of ingredient with {0} from ingredients pool with id {1} failed: {2}", ingredientsPoolId, ingredientId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new DeleteIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

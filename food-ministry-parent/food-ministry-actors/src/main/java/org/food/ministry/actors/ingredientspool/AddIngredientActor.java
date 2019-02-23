package org.food.ministry.actors.ingredientspool;

import java.text.MessageFormat;

import org.food.ministry.actors.ingredientspool.messages.AddIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.AddIngredientResultMessage;
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
 * This actor handles an attempt for adding ingredients to an ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddIngredientActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for ingredients pools
     */
    private IngredientsPoolDAO ingredientsPoolDao;
    /**
     * The data access object for ingredients pools
     */
    private IngredientDAO ingredientDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDao The data access object for ingredients pools
     */
    public AddIngredientActor(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        this.ingredientsPoolDao = ingredientsPoolDao;
        this.ingredientDao = ingredientDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @param ingredientDAO The data access object for ingredients pools
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDAO) {
        return Props.create(AddIngredientActor.class, () -> new AddIngredientActor(ingredientsPoolDao, ingredientDAO));
    }

    /**
     * Accepts a {@link AddIngredientMessage} and tries to add a ingredient to the ingredients
     * pool with the given information from the message. Afterwards a
     * {@link AddIngredientResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(AddIngredientMessage.class, this::addIngredient);

        return receiveBuilder.build();
    }

    /**
     * Tries to add a ingredient to the ingredients pool contained in provided the message
     * with the contained ingredients pool id
     * 
     * @param message The message containing all needed information for adding an
     *            ingredient to a ingredients pool
     */
    private void addIngredient(AddIngredientMessage message) {
        long ingredientsPoolId = message.getIngredientsPoolId();
        LOGGER.info("Adding an ingredient to ingredients pool with id {}", ingredientsPoolId);
        try {
            IngredientsPool ingredientsPool = ingredientsPoolDao.get(ingredientsPoolId);
            long ingredientId = UtilFunctions.generateUniqueId(ingredientsPoolDao, LOGGER);
            Ingredient ingredient = new Ingredient(ingredientId, message.getIngredientName(), message.getUnit(), message.isBasic());
            ingredientsPool.addIngredient(ingredient);
            ingredientDao.save(ingredient);
            ingredientsPoolDao.update(ingredientsPool);
            getSender().tell(new AddIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE), getSelf());
            LOGGER.info("Successfully added an ingredient with id {} to ingredients pool with id {}", ingredientId, ingredientsPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Adding an ingredient to ingredients pool with id {0} failed: {1}", ingredientsPoolId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new AddIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage), getSelf());
        }
    }
}

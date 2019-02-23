package org.food.ministry.actors.ingredient;

import java.text.MessageFormat;

import org.food.ministry.actors.ingredient.messages.GetIngredientMessage;
import org.food.ministry.actors.ingredient.messages.GetIngredientResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.model.Ingredient;
import org.food.ministry.model.Unit;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for getting ingredients
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for ingredients
     */
    private IngredientDAO ingredientDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param ingredientDao The data access object for ingredients
     */
    public GetIngredientActor(IngredientDAO ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientDao The data access object for ingredients
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientDAO ingredientDao) {
        return Props.create(GetIngredientActor.class, () -> new GetIngredientActor(ingredientDao));
    }

    /**
     * Accepts a {@link GetIngredientMessage} and tries to get the ingredient with
     * the given information from the message. Afterwards a
     * {@link GetIngredientResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientMessage.class, this::getIngredient);

        return receiveBuilder.build();
    }

    /**
     * Tries to get the ingredient contained in provided the message with the
     * contained ingredient id
     * 
     * @param message The message containing all needed information for getting an
     *            ingredient
     */
    private void getIngredient(GetIngredientMessage message) {
        long ingredientId = message.getIngredientId();
        LOGGER.info("Getting ingredient with id {}", ingredientId);
        try {
            Ingredient ingredient = ingredientDao.get(ingredientId);
            getSender().tell(new GetIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, ingredient.getName(),
                    ingredient.getUnit(), ingredient.isBasic()), getSelf());
            LOGGER.info("Successfully got ingredient with id {} ", ingredientId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting ingredient with id {0} failed: {1}", ingredientId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetIngredientResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, "", Unit.NONE, false), getSelf());
        }
    }
}

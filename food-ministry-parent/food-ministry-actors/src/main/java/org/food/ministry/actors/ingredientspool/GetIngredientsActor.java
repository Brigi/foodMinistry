package org.food.ministry.actors.ingredientspool;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.food.ministry.actors.ingredientspool.messages.GetIngredientsMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.model.IngredientsPool;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * This actor handles an attempt for getting ingredients from an ingredients pool.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetIngredientsActor extends AbstractActor {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    /**
     * The data access object for ingredients pools
     */
    private IngredientsPoolDAO ingredientsPoolDao;

    /**
     * Constructor setting the data access objects
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     */
    public GetIngredientsActor(IngredientsPoolDAO ingredientsPoolDao) {
        this.ingredientsPoolDao = ingredientsPoolDao;
    }

    /**
     * Gets the property to create an actor of this class
     * 
     * @param ingredientsPoolDao The data access object for ingredients pools
     * @return The property for creating an actor of this class
     */
    public static Props props(IngredientsPoolDAO ingredientsPoolDao) {
        return Props.create(GetIngredientsActor.class, () -> new GetIngredientsActor(ingredientsPoolDao));
    }

    /**
     * Accepts a {@link GetIngredientsMessage} and tries to get an ingredient from the
     * ingredients pool with the given information from the message. Afterwards a
     * {@link GetIngredientsResultMessage} is send back to the requesting actor
     * containing the results.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetIngredientsMessage.class, this::getIngredients);

        return receiveBuilder.build();
    }

    /**
     * Tries to get an ingredient from the ingredients pool contained in provided the message
     * with the contained user id
     * 
     * @param message The message containing all needed information for getting a
     *            ingredient from a ingredients pool
     */
    private void getIngredients(GetIngredientsMessage message) {
        long ingredientsPoolId = message.getIngredientsPoolId();
        LOGGER.info("Getting all ingredients of ingredients pool with id {}", ingredientsPoolId);
        try {
            IngredientsPool ingredientsPool = ingredientsPoolDao.get(ingredientsPoolId);
            Set<Long> ingredients = ingredientsPool.getIngredients().parallelStream().map(ingredient -> ingredient.getId()).collect(Collectors.toSet());
            getSender().tell(new GetIngredientsResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, ingredients), getSelf());
            LOGGER.info("Successfully got all ingredients of ingredients pool with id {}", ingredientsPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting all ingredients of ingredients pool with id {0} failed: {1}", ingredientsPoolId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetIngredientsResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, new HashSet<>()), getSelf());
        }
    }
}

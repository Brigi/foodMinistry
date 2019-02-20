package org.food.ministry.actors.recipespool;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.actors.recipespool.messages.GetRecipesMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.model.Recipe;
import org.food.ministry.model.RecipesPool;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class GetRecipesActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

    private RecipesPoolDAO recipesPoolDao;
    
    public GetRecipesActor(RecipesPoolDAO recipesPoolDao) {
        this.recipesPoolDao = recipesPoolDao;
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(RecipesPoolDAO recipesPoolDao) {
        return Props.create(GetRecipesActor.class, () -> new GetRecipesActor(recipesPoolDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesMessage.class, this::getRecipes);

        return receiveBuilder.build();
    }

    private void getRecipes(GetRecipesMessage message) {
        long recipesPoolId = message.getRecipesPoolId();
        LOGGER.info("Getting all recipes of recipes pool with id {}", recipesPoolId);
        try {
            RecipesPool recipePool = recipesPoolDao.get(recipesPoolId);
            Map<Long, String> recipes = recipePool.getRecipes().parallelStream().collect(Collectors.toMap(Recipe::getId, Recipe::getName));
            getSender().tell(new GetRecipesResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, recipes), getSelf());
            LOGGER.info("Successfully got all recipes of recipes pool with id {}", recipesPoolId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Getting all recipes of recipes pool with id {0} failed: {1}", recipesPoolId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetRecipesResultMessage(IDGenerator.getRandomID(), message.getId(), false, errorMessage, new HashMap<>()), getSelf());
        }
    }
}

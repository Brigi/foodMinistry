package org.food.ministry.actors.household;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.actors.household.messages.GetRecipesMessage;
import org.food.ministry.actors.household.messages.GetRecipesResultMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.actors.util.UtilFunctions;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.model.Household;
import org.food.ministry.model.Recipe;

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

    private HouseholdDAO householdDao;
    
    public GetRecipesActor(HouseholdDAO householdDao) {
        this.householdDao = householdDao;
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(GetRecipesActor.class, () -> new GetRecipesActor(householdDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesMessage.class, this::getRecipes);

        return receiveBuilder.build();
    }

    private void getRecipes(GetRecipesMessage message) {
        long householdId = message.getHouseholdId();
        LOGGER.info("Getting all recipes for household with id {}", householdId);
        try {
            Household household = householdDao.get(householdId);
            Map<Long, String> recipes = household.getRecipes().parallelStream().collect(Collectors.toMap(Recipe::getId, Recipe::getName));

            getSender().tell(new GetRecipesResultMessage(IDGenerator.getRandomID(), message.getId(), true, Constants.NO_ERROR_MESSAGE, recipes), getSelf());
            LOGGER.info("Successfully got recipes for household with id {}", householdId);
        } catch(Exception e) {
            final String errorMessage = MessageFormat.format("Adding a household for household id {0} failed: {1}", householdId, e.getMessage());
            LOGGER.info("{}\r\n{}", errorMessage, UtilFunctions.getStacktraceAsString(e));
            getSender().tell(new GetHouseholdsResultMessage(IDGenerator.getRandomID(), message.getId(), true, errorMessage, new HashMap<>()), getSelf());
        }
    }
}

package org.food.ministry.actors.household;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.household.HouseholdDAO;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class HouseholdActor extends AbstractActor {
    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);
    
    /**
     * The actor child for recipes handling
     */
    private final ActorRef getRecipesPoolChild;
    
    public HouseholdActor(HouseholdDAO householdDao) {
        getRecipesPoolChild = getContext().actorOf(GetRecipesPoolActor.props(householdDao), "getRecipesPoolActor");
    }
    
    /**
     * Gets the property to create an actor of this class
     * 
     * @return The property for creating an actor of this class
     */
    public static Props props(HouseholdDAO householdDao) {
        return Props.create(HouseholdActor.class, () -> new HouseholdActor(householdDao));
    }
    
    @Override
    public Receive createReceive() {
        ReceiveBuilder receiveBuilder = receiveBuilder();
        receiveBuilder.match(GetRecipesMessage.class, this::delegateToGetRecipesActor);

        return receiveBuilder.build();
    }
    
    private void delegateToGetRecipesActor(GetRecipesMessage message) {
        LOGGER.info("Getting recipes pool with message {}", message.getId());
        getRecipesPoolChild.forward(message, getContext());
        getSender().tell(new DelegateMessage(IDGenerator.getRandomID(), message.getId()), getSelf());
    }
}

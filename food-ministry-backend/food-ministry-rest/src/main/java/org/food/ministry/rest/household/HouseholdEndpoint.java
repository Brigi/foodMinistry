package org.food.ministry.rest.household;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.household.HouseholdActor;
import org.food.ministry.actors.household.messages.GetFoodInventoryMessage;
import org.food.ministry.actors.household.messages.GetFoodInventoryResultMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolMessage;
import org.food.ministry.actors.household.messages.GetIngredientsPoolResultMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolMessage;
import org.food.ministry.actors.household.messages.GetRecipesPoolResultMessage;
import org.food.ministry.actors.household.messages.GetShoppingListMessage;
import org.food.ministry.actors.household.messages.GetShoppingListResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.household.json.GetFoodInventoryJSON;
import org.food.ministry.rest.household.json.GetFoodInventoryResultJSON;
import org.food.ministry.rest.household.json.GetIngredientsPoolJSON;
import org.food.ministry.rest.household.json.GetIngredientsPoolResultJSON;
import org.food.ministry.rest.household.json.GetRecipesPoolJSON;
import org.food.ministry.rest.household.json.GetRecipesPoolResultJSON;
import org.food.ministry.rest.household.json.GetShoppingListJSON;
import org.food.ministry.rest.household.json.GetShoppingListResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class HouseholdEndpoint extends AEndpoint{

    private ActorRef householdActorSuppressDelegate;
    
    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("household");
    
    public HouseholdEndpoint(FoodMinistryServer server, HouseholdDAO householdDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(HouseholdActor.props(householdDao), "household-actor");
        householdActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "household-actor-suppressed-delegate");
        
    }

    @Override
    public Route getRoute() {
        return getServer().concat(createGetFoodInventoryRoute(), createGetIngredientsPoolRoute(), createGetRecipesPoolRoute(), createGetShoppingListRoute());
    }

    private Route createGetShoppingListRoute() {
        PathMatcher0 pathMatcher = BASE_PATH.slash("shoppinglist");
        return RouteUtil.createGetRoute(getServer(), pathMatcher, GetShoppingListJSON.class, this::getShoppingList);
    }
    
    private Route getShoppingList(GetShoppingListJSON getShoppingListJSON) {
        getLogger().info("Getting shopping list from user with id {}", getShoppingListJSON.getUserId());
        CompletableFuture<Object> getShoppingListFuture = Patterns.ask(householdActorSuppressDelegate, new GetShoppingListMessage(IDGenerator.getRandomID(), getShoppingListJSON.getHouseholdId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetShoppingListResultMessage resultMessage = (GetShoppingListResultMessage) getShoppingListFuture.join();
        if(resultMessage.isSuccessful()) {
            GetShoppingListResultJSON resultJSON = new GetShoppingListResultJSON(resultMessage.getShoppingListId());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetShoppingListResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createGetRecipesPoolRoute() {
        getLogger().info("Creating get recipes pool endpoint");
        PathMatcher0 pathMatcher = BASE_PATH.slash("recipespool");
        return RouteUtil.createGetRoute(getServer(), pathMatcher, GetRecipesPoolJSON.class, this::getRecipesPool);
    }

    private Route getRecipesPool(GetRecipesPoolJSON getRecipesPoolJSON) {
        getLogger().info("Getting recipes pool from user with id {}", getRecipesPoolJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns.ask(householdActorSuppressDelegate, new GetRecipesPoolMessage(IDGenerator.getRandomID(), getRecipesPoolJSON.getHouseholdId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetRecipesPoolResultMessage resultMessage = (GetRecipesPoolResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetRecipesPoolResultJSON resultJSON = new GetRecipesPoolResultJSON(resultMessage.getRecipesPoolId());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetRecipesPoolResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createGetIngredientsPoolRoute() {
        getLogger().info("Creating get ingredients pool endpoint");
        PathMatcher0 pathMatcher = BASE_PATH.slash("ingredientspool");
        return RouteUtil.createGetRoute(getServer(), pathMatcher, GetIngredientsPoolJSON.class, this::getIngredientsPool);
    }
    
    private Route getIngredientsPool(GetIngredientsPoolJSON getIngredientsPoolJSON) {
        getLogger().info("Getting ingredients pool from user with id {}", getIngredientsPoolJSON.getUserId());
        CompletableFuture<Object> getIngredientsPoolFuture = Patterns.ask(householdActorSuppressDelegate, new GetIngredientsPoolMessage(IDGenerator.getRandomID(), getIngredientsPoolJSON.getHouseholdId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetIngredientsPoolResultMessage resultMessage = (GetIngredientsPoolResultMessage) getIngredientsPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetIngredientsPoolResultJSON resultJSON = new GetIngredientsPoolResultJSON(resultMessage.getIngredientsPoolId());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetIngredientsPoolResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }

    private Route createGetFoodInventoryRoute() {
        getLogger().info("Creating get food inventory endpoint");
        PathMatcher0 pathMatcher = BASE_PATH.slash("foodinventory");
        return RouteUtil.createGetRoute(getServer(), pathMatcher, GetFoodInventoryJSON.class, this::getFoodInventory);
    }
    
    private Route getFoodInventory(GetFoodInventoryJSON getFoodInventoryJSON) {
        getLogger().info("Getting food inventory from user with id {}", getFoodInventoryJSON.getUserId());
        CompletableFuture<Object> getFoodInventoryFuture = Patterns.ask(householdActorSuppressDelegate, new GetFoodInventoryMessage(IDGenerator.getRandomID(), getFoodInventoryJSON.getHouseholdId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetFoodInventoryResultMessage resultMessage = (GetFoodInventoryResultMessage) getFoodInventoryFuture.join();
        if(resultMessage.isSuccessful()) {
            GetFoodInventoryResultJSON resultJSON = new GetFoodInventoryResultJSON(resultMessage.getFoodInventoryId());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetFoodInventoryResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

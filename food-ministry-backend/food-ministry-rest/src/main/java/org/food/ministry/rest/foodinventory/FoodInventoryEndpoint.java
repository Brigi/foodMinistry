package org.food.ministry.rest.foodinventory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.foodinventory.FoodInventoryActor;
import org.food.ministry.actors.foodinventory.messages.AddIngredientMessage;
import org.food.ministry.actors.foodinventory.messages.AddIngredientResultMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsMessage;
import org.food.ministry.actors.foodinventory.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.foodinventory.json.AddIngredientJSON;
import org.food.ministry.rest.foodinventory.json.GetIngredientsJSON;
import org.food.ministry.rest.foodinventory.json.GetIngredientsResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class FoodInventoryEndpoint extends AEndpoint{

    private ActorRef foodInventoryActorSuppressDelegate;
    
    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("foodinventory");
    
    public FoodInventoryEndpoint(FoodMinistryServer server, FoodInventoryDAO foodInventoryDao, IngredientDAO ingredientDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(FoodInventoryActor.props(foodInventoryDao, ingredientDao), "foodinventory-actor");
        foodInventoryActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "foodinventory-actor-suppressed-delegate");
        
    }

    @Override
    public Route getRoute() {
        return getServer().concat(createAddIngredientsRoute(), createGetIngredientsRoute());
    }

    private Route createAddIngredientsRoute() {
        return RouteUtil.createPutRoute(getServer(), BASE_PATH, AddIngredientJSON.class, this::addIngredient);
    }
    
    private Route addIngredient(AddIngredientJSON addIngredientsJSON) {
        getLogger().info("Adding ingredient to food inventory for user with id {}", addIngredientsJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns.ask(foodInventoryActorSuppressDelegate, new AddIngredientMessage(IDGenerator.getRandomID(), addIngredientsJSON.getFoodInventoryId(), addIngredientsJSON.getIngredient(), addIngredientsJSON.getAmount()), Duration.ofMillis(1000)).toCompletableFuture();
        AddIngredientResultMessage resultMessage = (AddIngredientResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.CREATED);
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createGetIngredientsRoute() {
        return RouteUtil.createGetRoute(getServer(), BASE_PATH, GetIngredientsJSON.class, this::getIngredients);
    }
    
    private Route getIngredients(GetIngredientsJSON getIngredientsJSON) {
        getLogger().info("Getting ingredients of food inventory from user with id {}", getIngredientsJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns.ask(foodInventoryActorSuppressDelegate, new GetIngredientsMessage(IDGenerator.getRandomID(), getIngredientsJSON.getFoodInventoryId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetIngredientsResultMessage resultMessage = (GetIngredientsResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetIngredientsResultJSON resultJSON = new GetIngredientsResultJSON(resultMessage.getIngredientsWithAmount());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetIngredientsResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

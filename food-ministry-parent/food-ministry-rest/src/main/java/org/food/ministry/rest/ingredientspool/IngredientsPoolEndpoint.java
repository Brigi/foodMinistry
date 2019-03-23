package org.food.ministry.rest.ingredientspool;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.ingredientspool.IngredientsPoolActor;
import org.food.ministry.actors.ingredientspool.messages.AddIngredientMessage;
import org.food.ministry.actors.ingredientspool.messages.AddIngredientResultMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsMessage;
import org.food.ministry.actors.ingredientspool.messages.GetIngredientsResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.ingredientspool.json.AddIngredientJSON;
import org.food.ministry.rest.ingredientspool.json.GetIngredientsJSON;
import org.food.ministry.rest.ingredientspool.json.GetIngredientsResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class IngredientsPoolEndpoint extends AEndpoint {

    private ActorRef ingredientsPoolActorSuppressDelegate;

    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("ingredientspool");

    public IngredientsPoolEndpoint(FoodMinistryServer server, IngredientsPoolDAO ingredientsPoolDao, IngredientDAO ingredientDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(IngredientsPoolActor.props(ingredientsPoolDao, ingredientDao), "ingredientspool-actor");
        ingredientsPoolActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "ingredientspool-actor-suppressed-delegate");

    }

    @Override
    public Route getRoute() {
        return getServer().concat(createAddIngredientsRoute(), createGetIngredientsRoute());
    }

    private Route createAddIngredientsRoute() {
        return RouteUtil.createPutRoute(getServer(), BASE_PATH, AddIngredientJSON.class, this::addIngredient);
    }

    private Route addIngredient(AddIngredientJSON addIngredientsJSON) {
        getLogger().info("Adding ingredient to ingredients pool for user with id {}", addIngredientsJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(ingredientsPoolActorSuppressDelegate, new AddIngredientMessage(IDGenerator.getRandomID(), addIngredientsJSON.getIngredientsPoolId(),
                        addIngredientsJSON.getIngredientName(), addIngredientsJSON.getUnit(), addIngredientsJSON.isBasic()), Duration.ofMillis(1000))
                .toCompletableFuture();
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
        getLogger().info("Getting ingredients of ingredients pool from user with id {}", getIngredientsJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(ingredientsPoolActorSuppressDelegate, new GetIngredientsMessage(IDGenerator.getRandomID(), getIngredientsJSON.getIngredientsPoolId()), Duration.ofMillis(1000))
                .toCompletableFuture();
        GetIngredientsResultMessage resultMessage = (GetIngredientsResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetIngredientsResultJSON resultJSON = new GetIngredientsResultJSON(resultMessage.getIngredientIds());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetIngredientsResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

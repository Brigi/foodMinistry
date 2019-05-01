package org.food.ministry.rest.ingredient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.ingredient.IngredientActor;
import org.food.ministry.actors.ingredient.messages.GetIngredientMessage;
import org.food.ministry.actors.ingredient.messages.GetIngredientResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.ingredient.json.GetIngredientJSON;
import org.food.ministry.rest.ingredient.json.GetIngredientResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class IngredientEndpoint extends AEndpoint {

    private ActorRef ingredientActorSuppressDelegate;

    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("ingredient");

    public IngredientEndpoint(FoodMinistryServer server, IngredientDAO ingredientDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(IngredientActor.props(ingredientDao), "ingredient-actor");
        ingredientActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "ingredient-actor-suppressed-delegate");
    }

    @Override
    public Route getRoute() {
        return getServer().concat(createGetIngredientRoute());
    }

    private Route createGetIngredientRoute() {
        return RouteUtil.createGetRoute(getServer(), BASE_PATH, GetIngredientJSON.class, this::getIngredient);
    }

    private Route getIngredient(GetIngredientJSON getIngredientsJSON) {
        getLogger().info("Getting ingredient for user with id {}", getIngredientsJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(ingredientActorSuppressDelegate, new GetIngredientMessage(IDGenerator.getRandomID(), getIngredientsJSON.getIngredientId()), Duration.ofMillis(1000))
                .toCompletableFuture();
        GetIngredientResultMessage resultMessage = (GetIngredientResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetIngredientResultJSON resultJSON = new GetIngredientResultJSON(resultMessage.getIngredientName(), resultMessage.getIngredientUnit(), resultMessage.isBasicIngredient());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetIngredientResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

package org.food.ministry.rest.recipe;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.recipe.RecipeActor;
import org.food.ministry.actors.recipe.messages.GetRecipeMessage;
import org.food.ministry.actors.recipe.messages.GetRecipeResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.recipe.json.GetRecipeJSON;
import org.food.ministry.rest.recipe.json.GetRecipeResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class RecipeEndpoint extends AEndpoint {

    private ActorRef recipeActorSuppressDelegate;

    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("recipe");

    public RecipeEndpoint(FoodMinistryServer server, RecipeDAO recipeDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(RecipeActor.props(recipeDao), "recipe-actor");
        recipeActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "recipe-actor-suppressed-delegate");
    }

    @Override
    public Route getRoute() {
        return getServer().concat(createGetrecipeRoute());
    }

    private Route createGetrecipeRoute() {
        return RouteUtil.createGetRoute(getServer(), BASE_PATH, GetRecipeJSON.class, this::getRecipe);
    }

    private Route getRecipe(GetRecipeJSON getRecipesJSON) {
        getLogger().info("Getting recipe for user with id {}", getRecipesJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(recipeActorSuppressDelegate, new GetRecipeMessage(IDGenerator.getRandomID(), getRecipesJSON.getRecipeId()), Duration.ofMillis(1000))
                .toCompletableFuture();
        GetRecipeResultMessage resultMessage = (GetRecipeResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetRecipeResultJSON resultJSON = new GetRecipeResultJSON(resultMessage.getRecipeName(), resultMessage.getRecipeDescription(), resultMessage.getIngredientsWithAmount());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetRecipeResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

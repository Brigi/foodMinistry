package org.food.ministry.rest.recipespool;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.recipespool.RecipesPoolActor;
import org.food.ministry.actors.recipespool.messages.AddRecipeMessage;
import org.food.ministry.actors.recipespool.messages.AddRecipeResultMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesMessage;
import org.food.ministry.actors.recipespool.messages.GetRecipesResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.ingredient.IngredientDAO;
import org.food.ministry.data.access.recipe.RecipeDAO;
import org.food.ministry.data.access.recipespool.RecipesPoolDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.recipespool.json.AddRecipeJSON;
import org.food.ministry.rest.recipespool.json.GetRecipesJSON;
import org.food.ministry.rest.recipespool.json.GetRecipesResultJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class RecipesPoolEndpoint extends AEndpoint {

    private ActorRef recipesPoolActorSuppressDelegate;

    private static final PathMatcher0 BASE_PATH = PathMatchers.segment("user").slash("recipespool");

    public RecipesPoolEndpoint(FoodMinistryServer server, RecipesPoolDAO recipesPoolDao, RecipeDAO recipeDao, IngredientDAO ingredientDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(RecipesPoolActor.props(recipesPoolDao, recipeDao, ingredientDao), "recipesPool-actor");
        recipesPoolActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "recipesPool-actor-suppressed-delegate");

    }

    @Override
    public Route getRoute() {
        return getServer().concat(createAddRecipesRoute(), createGetRecipesRoute());
    }

    private Route createAddRecipesRoute() {
        return RouteUtil.createPutRoute(getServer(), BASE_PATH, AddRecipeJSON.class, this::addRecipe);
    }

    private Route addRecipe(AddRecipeJSON addRecipesJSON) {
        getLogger().info("Adding recipe to recipes pool for user with id {}", addRecipesJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(recipesPoolActorSuppressDelegate, new AddRecipeMessage(IDGenerator.getRandomID(), addRecipesJSON.getRecipesPoolId(),
                        addRecipesJSON.getRecipeName(), addRecipesJSON.getIngredientsWithAmount(), addRecipesJSON.getRecipeDescription()), Duration.ofMillis(1000))
                .toCompletableFuture();
        AddRecipeResultMessage resultMessage = (AddRecipeResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.CREATED);
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }

    private Route createGetRecipesRoute() {
        return RouteUtil.createGetRoute(getServer(), BASE_PATH, GetRecipesJSON.class, this::getRecipes);
    }

    private Route getRecipes(GetRecipesJSON getRecipesJSON) {
        getLogger().info("Getting recipes of recipes pool from user with id {}", getRecipesJSON.getUserId());
        CompletableFuture<Object> getRecipesPoolFuture = Patterns
                .ask(recipesPoolActorSuppressDelegate, new GetRecipesMessage(IDGenerator.getRandomID(), getRecipesJSON.getRecipesPoolId()), Duration.ofMillis(1000))
                .toCompletableFuture();
        GetRecipesResultMessage resultMessage = (GetRecipesResultMessage) getRecipesPoolFuture.join();
        if(resultMessage.isSuccessful()) {
            GetRecipesResultJSON resultJSON = new GetRecipesResultJSON(resultMessage.getRecipesIdsWithName());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetRecipesResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

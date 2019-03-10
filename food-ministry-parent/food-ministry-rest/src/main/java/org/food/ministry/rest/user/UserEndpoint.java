package org.food.ministry.rest.user;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.user.UserActor;
import org.food.ministry.actors.user.messages.AddHouseholdMessage;
import org.food.ministry.actors.user.messages.AddHouseholdResultMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsResultMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.user.messages.RegisterResultMessage;
import org.food.ministry.actors.user.messages.RemoveHouseholdMessage;
import org.food.ministry.actors.user.messages.RemoveHouseholdResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.user.json.AddHouseholdJSON;
import org.food.ministry.rest.user.json.GetHouseholdsJSON;
import org.food.ministry.rest.user.json.GetHouseholdsResultJSON;
import org.food.ministry.rest.user.json.LoginUserJSON;
import org.food.ministry.rest.user.json.LoginUserResultJSON;
import org.food.ministry.rest.user.json.RegisterUserJSON;
import org.food.ministry.rest.user.json.RemoveHouseholdJSON;
import org.food.ministry.rest.util.MessageUtil;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class UserEndpoint extends AEndpoint {

    private ActorRef userActorSuppressDelegate;
    
    public UserEndpoint(FoodMinistryServer server, UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        super(server);
        ActorRef userActor = server.getSystem().actorOf(UserActor.props(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao), "user-actor");
        userActorSuppressDelegate = server.getSystem().actorOf(SuppressDelegateMessageActor.props(userActor), "user-actor-suppressed-delegate");
        
    }
    
    @Override
    public Route getRoute() {
        return getServer().concat(createLoginRoute(), createRegisterRoute(), createAddHouseholdRoute(), createGetHouseholdsRoute(), createRemoveHouseholdRoute());
    }
    
    private Route createRegisterRoute() {
        getLogger().info("Creating register endpoint");
        return RouteUtil.createPutRoute(getServer(), "register", RegisterUserJSON.class, this::registerUser);
    }
    
    private Route registerUser(RegisterUserJSON json) {
        getLogger().info("Registering user {}", json.getUsername());
        CompletableFuture<Object> userFuture = Patterns.ask(userActorSuppressDelegate, new RegisterMessage(IDGenerator.getRandomID(), json.getUsername(), json.getEmailAddress(), json.getPassword()), Duration.ofMillis(1000)).toCompletableFuture();
        RegisterResultMessage resultMessage = (RegisterResultMessage) userFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.CREATED);
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createLoginRoute() {
        getLogger().info("Creating login endpoint");
        return RouteUtil.createGetRoute(getServer(), "login", LoginUserJSON.class, this::loginUser);
    }
    
    private Route loginUser(LoginUserJSON loginJSON) {
        getLogger().info("Logging in user {}", loginJSON.getEmailAddress());
        CompletableFuture<Object> userFuture = Patterns.ask(userActorSuppressDelegate, new LoginMessage(IDGenerator.getRandomID(), loginJSON.getEmailAddress(), loginJSON.getPassword()), Duration.ofMillis(1000)).toCompletableFuture();
        LoginResultMessage resultMessage = (LoginResultMessage) userFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.OK, new LoginUserResultJSON(resultMessage.getUserId()), Jackson.<LoginUserResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createAddHouseholdRoute() {
        getLogger().info("Creating add household endpoint");
        PathMatcher0 pathMatcher = PathMatchers.segment("user").slash("household");
        return RouteUtil.createPutRoute(getServer(), pathMatcher, AddHouseholdJSON.class, this::addHousehold);
    }
    
    private Route addHousehold(AddHouseholdJSON addHouseholdJSON) {
        getLogger().info("Adding household to user with id {}", addHouseholdJSON.getUserId());
        CompletableFuture<Object> addHouseholdFuture = Patterns.ask(userActorSuppressDelegate, new AddHouseholdMessage(IDGenerator.getRandomID(), addHouseholdJSON.getUserId(), addHouseholdJSON.getHouseholdName()), Duration.ofMillis(1000)).toCompletableFuture();
        AddHouseholdResultMessage resultMessage = (AddHouseholdResultMessage) addHouseholdFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.CREATED);
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createGetHouseholdsRoute() {
        getLogger().info("Creating get households endpoint");
        PathMatcher0 pathMatcher = PathMatchers.segment("user").slash("household");
        return RouteUtil.createGetRoute(getServer(), pathMatcher, GetHouseholdsJSON.class, this::getHouseholds);
    }
    
    private Route getHouseholds(GetHouseholdsJSON getHouseholdJSON) {
        getLogger().info("Getting households from user with id {}", getHouseholdJSON.getUserId());
        CompletableFuture<Object> addHouseholdFuture = Patterns.ask(userActorSuppressDelegate, new GetHouseholdsMessage(IDGenerator.getRandomID(), getHouseholdJSON.getUserId()), Duration.ofMillis(1000)).toCompletableFuture();
        GetHouseholdsResultMessage resultMessage = (GetHouseholdsResultMessage) addHouseholdFuture.join();
        if(resultMessage.isSuccessful()) {
            GetHouseholdsResultJSON resultJSON = new GetHouseholdsResultJSON(resultMessage.getHouseholdIdsWithName());
            return getServer().complete(StatusCodes.OK, resultJSON, Jackson.<GetHouseholdsResultJSON>marshaller());
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
    
    private Route createRemoveHouseholdRoute() {
        getLogger().info("Creating remove households endpoint");
        PathMatcher0 pathMatcher = PathMatchers.segment("user").slash("household");
        return RouteUtil.createDeleteRoute(getServer(), pathMatcher, RemoveHouseholdJSON.class, this::removeHousehold);
    }
    
    private Route removeHousehold(RemoveHouseholdJSON removeHouseholdJSON) {
        getLogger().info("Removing households from user with id {}", removeHouseholdJSON.getUserId());
        CompletableFuture<Object> addHouseholdFuture = Patterns.ask(userActorSuppressDelegate, new RemoveHouseholdMessage(IDGenerator.getRandomID(), removeHouseholdJSON.getUserId(), removeHouseholdJSON.getHouseholdId()), Duration.ofMillis(1000)).toCompletableFuture();
        RemoveHouseholdResultMessage resultMessage = (RemoveHouseholdResultMessage) addHouseholdFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.OK);
        }
        return MessageUtil.createErrorResponse(getServer(), resultMessage.getErrorMessage());
    }
}

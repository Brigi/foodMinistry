package org.food.ministry.rest.user;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.user.UserActor;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.user.messages.RegisterResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.actors.SuppressDelegateMessageActor;
import org.food.ministry.rest.user.json.LoginUserJSON;
import org.food.ministry.rest.user.json.RegisterUserJSON;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.model.StatusCodes;
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
        return getServer().concat(createLoginRoute(), createRegisterRoute());
    }
    
    private Route createRegisterRoute() {
        getLogger().info("Creating register endpoint");
        return RouteUtil.createPutRoute(getServer(), "user", RegisterUserJSON.class, this::registerUser);
    }
    
    private Route registerUser(RegisterUserJSON json) {
        getLogger().info("Registering user {}", json.getUsername());
        CompletableFuture<Object> userFuture = Patterns.ask(userActorSuppressDelegate, new RegisterMessage(IDGenerator.getRandomID(), json.getUsername(), json.getEmailAddress(), json.getPassword()), Duration.ofMillis(1000)).toCompletableFuture();
        RegisterResultMessage resultMessage = (RegisterResultMessage) userFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.CREATED, resultMessage.getErrorMessage());
        }
        return getServer().complete(StatusCodes.BAD_REQUEST, resultMessage.getErrorMessage());
    }
    
    private Route createLoginRoute() {
        getLogger().info("Creating login endpoint");
        return RouteUtil.createGetRoute(getServer(), "user", LoginUserJSON.class, this::loginUser);
    }
    
    private Route loginUser(LoginUserJSON loginJSON) {
        getLogger().info("Loging in user {}", loginJSON.getEmailAddress());
        CompletableFuture<Object> userFuture = Patterns.ask(userActorSuppressDelegate, new LoginMessage(IDGenerator.getRandomID(), loginJSON.getEmailAddress(), loginJSON.getPassword()), Duration.ofMillis(1000)).toCompletableFuture();
        LoginResultMessage resultMessage = (LoginResultMessage) userFuture.join();
        if(resultMessage.isSuccessful()) {
            return getServer().complete(StatusCodes.OK, resultMessage.getErrorMessage());
        }
        return getServer().complete(StatusCodes.BAD_REQUEST, resultMessage.getErrorMessage());
    }
}

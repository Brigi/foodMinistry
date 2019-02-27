package org.food.ministry.rest.user;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.food.ministry.actors.user.UserActor;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.util.RouteUtil;

import akka.actor.ActorRef;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class UserEndpoint extends AEndpoint {

    private ActorRef userActor;

    public UserEndpoint(FoodMinistryServer server, UserDAO userDao, HouseholdDAO householdDao, FoodInventoryDAO foodInventoryDao, ShoppingListDAO shoppingListDao,
            IngredientsPoolDAO ingredientsPoolDao) {
        super(server);
        userActor = server.getSystem().actorOf(UserActor.props(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao), "user-actor");
    }

    @Override
    public Route getRoute() {
        return createLoginRoute();
    }
    
    private Route createLoginRoute() {
        getLogger().info("Creating login endpoint");
        return RouteUtil.createGetRouteWithPrefix(getServer(), "user", this::loginUser);
    }
    
    private Route loginUser(Long id) {
        getLogger().info("Loging in user with id " + id);
        CompletableFuture<Object> userFuture = Patterns.ask(userActor, new LoginMessage(IDGenerator.getRandomID(), "", "", ""), Duration.ofMillis(1000)).toCompletableFuture();
        LoginResultMessage resultMessage = (LoginResultMessage) userFuture.join();
        return getServer().complete(MessageFormat.format("<h1>Say hello to {}</h1>", resultMessage.isSuccessful()));
    }
}

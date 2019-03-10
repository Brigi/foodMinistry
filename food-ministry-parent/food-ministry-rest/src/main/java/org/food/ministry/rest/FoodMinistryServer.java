package org.food.ministry.rest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.StorageType;
import org.food.ministry.data.access.factory.DAOFactory;
import org.food.ministry.rest.household.HouseholdEndpoint;
import org.food.ministry.rest.shutdown.ShutdownEndpoint;
import org.food.ministry.rest.user.UserEndpoint;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

public class FoodMinistryServer extends AllDirectives {

    /**
     * Logger logging stuff
     */
    private final LoggingAdapter logger;
    
    private final ActorSystem system;
    
    private final DAOFactory daoFactory;

    private final Set<IEndpoint> endpoints;
    
    private CompletionStage<ServerBinding> binding;
    
    public FoodMinistryServer(StorageType storageType) {
        this.system = ActorSystem.create("food-ministry-actor-system");
        logger = Logging.getLogger(system, this);
        IDGenerator.initializeGeneratorActor(system);
        this.daoFactory = DAOFactory.getDAOFactory(storageType);
        this.endpoints = new HashSet<>();        
    }

    public ActorSystem getSystem() {
        return this.system;
    }
    
    public CompletionStage<ServerBinding> getServerBinding() {
        return this.binding;
    }

    public void start(String hostAddress, int port) {
        logger.info("Starting server...");
        Http http = Http.get(system);
        ActorMaterializer materializer = ActorMaterializer.create(system);
        createShutdownEndpoint();
        createHouseholdEndpoint();
        createUserEndpoint();
        Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = prepareRoutes().flow(system, materializer);
        binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost(hostAddress, port), materializer);
        logger.info("Server successfully started");
    }
    
    private Route prepareRoutes() {
        Iterator<IEndpoint> iterator = endpoints.iterator();
        Route route = null;
        if(iterator.hasNext()) {
            route = iterator.next().getRoute();
        }
        while(iterator.hasNext()) {
            route = concat(route, iterator.next().getRoute());
        }
        
        return route;
    }

    private void createUserEndpoint() {
        logger.info("Creating user endpoint...");
        endpoints.add(new UserEndpoint(this, daoFactory.getUserDAO(), daoFactory.getHouseholdDAO(), daoFactory.getFoodInventoryDAO(), daoFactory.getShoppingListDAO(), daoFactory.getIngredientsPoolDAO()));
        logger.info("User endpoint successfully created");
    }

    private void createShutdownEndpoint() {
        logger.info("Creating shutdown endpoint...");
        endpoints.add(new ShutdownEndpoint(this));
        logger.info("Shutdown endpoint successfully created");
    }
    
    private void createHouseholdEndpoint() {
        logger.info("Creating household endpoint...");
        endpoints.add(new HouseholdEndpoint(this, daoFactory.getHouseholdDAO()));
        logger.info("Household endpoint successfully created");
    }
}

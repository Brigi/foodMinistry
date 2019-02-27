package org.food.ministry.rest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletionStage;

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

    private final Set<IEndpoint> endpoints;
    
    private CompletionStage<ServerBinding> binding;
    
    public FoodMinistryServer() {
        this.system = ActorSystem.create("food-ministry-actor-system");
        logger = Logging.getLogger(system, this);
        this.endpoints = new HashSet<>();
    }

    public ActorSystem getSystem() {
        return this.system;
    }
    
    public CompletionStage<ServerBinding> getServerBinding() {
        return this.binding;
    }

    public void start() {
        logger.info("Starting server...");
        Http http = Http.get(system);
        ActorMaterializer materializer = ActorMaterializer.create(system);
        createUserEndpoint();
        createShutdownEndpoint();
        Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = prepareRoutes().flow(system, materializer);
        binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
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
        endpoints.add(new UserEndpoint(this, null, null ,null, null, null));
        logger.info("User endpoint successfully created");
    }

    private void createShutdownEndpoint() {
        logger.info("Creating shutdown endpoint...");
        endpoints.add(new ShutdownEndpoint(this));
        logger.info("Shutdown endpoint successfully created");
    }
}

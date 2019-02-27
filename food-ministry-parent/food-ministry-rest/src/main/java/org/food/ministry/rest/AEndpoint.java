package org.food.ministry.rest;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.server.Route;

public abstract class AEndpoint implements IEndpoint {

    
    /**
     * Logger logging stuff
     */
    private final LoggingAdapter logger;
    
    private final FoodMinistryServer server;
    
    public AEndpoint(FoodMinistryServer server) {
        this.server = server;
        this.logger = Logging.getLogger(server.getSystem(), this);
    }
    
    protected FoodMinistryServer getServer() {
        return this.server;
    }
    
    protected LoggingAdapter getLogger() {
        return logger;
    }
    
    public abstract Route getRoute();
}

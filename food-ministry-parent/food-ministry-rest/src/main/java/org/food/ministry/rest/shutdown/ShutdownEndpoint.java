package org.food.ministry.rest.shutdown;

import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.util.RouteUtil;

import akka.Done;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;

public class ShutdownEndpoint extends AEndpoint {

    public ShutdownEndpoint(FoodMinistryServer server) {
        super(server);
    }

    @Override
    public Route getRoute() {
        return RouteUtil.createGetRoute(getServer(), "Shutdown", this::terminateSystem);
    }
    
    private Route terminateSystem() {
        getLogger().info("Shutting down server...");
        getServer().getServerBinding().thenCompose(ServerBinding::unbind).thenAccept(this::terminateSystem);
        return getServer().complete("<h1>Server shutting down!</h1>");
    }
    
    private void terminateSystem(Done done) {
        getServer().getSystem().terminate();
        getLogger().info("Server successfully shut down!");
    }
}

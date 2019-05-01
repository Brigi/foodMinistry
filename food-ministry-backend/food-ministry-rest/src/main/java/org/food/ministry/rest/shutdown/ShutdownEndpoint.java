package org.food.ministry.rest.shutdown;

import java.util.concurrent.TimeUnit;

import org.food.ministry.rest.AEndpoint;
import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.shutdown.json.ShutdownJSON;
import org.food.ministry.rest.util.RouteUtil;

import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;

public class ShutdownEndpoint extends AEndpoint {

    public ShutdownEndpoint(FoodMinistryServer server) {
        super(server);
    }

    @Override
    public Route getRoute() {
        return RouteUtil.createGetRoute(getServer(), "shutdown", ShutdownJSON.class, this::terminateSystem);
    }
    
    private Route terminateSystem(ShutdownJSON shutdownJSON) {
        String errorMessage = null;
        try {
            getLogger().info("Shutting down server. Reason: {}", shutdownJSON.getReason());
            ServerBinding serverBinding = getServer().getServerBinding().toCompletableFuture().get(1, TimeUnit.SECONDS);
            serverBinding.unbind().toCompletableFuture().get(2, TimeUnit.SECONDS);
            getServer().getSystem().terminate();
            getLogger().info("Server successfully shut down!");
            return getServer().complete(StatusCodes.OK);
        } catch(Exception e) {
            errorMessage = e.getMessage();
        }
        return getServer().complete(StatusCodes.INTERNAL_SERVER_ERROR, errorMessage);
    }
}

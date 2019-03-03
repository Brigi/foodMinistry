package org.food.ministry.rest;

import org.food.ministry.data.access.StorageType;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FoodMinistryInMemoryLauncher {
    
    private static final String DEFAULT_HOST_ADDRESS = "localhost";
    
    private static final int DEFAULT_PORT = 8080;
    
    public static void main(String... args) {
        new FoodMinistryInMemoryLauncher().runServer();
    }
    
    public void runServer() {
        runServer(DEFAULT_HOST_ADDRESS, DEFAULT_PORT);
    }
    
    public void runServer(String hostAddress, int port) {
        FoodMinistryServer server = new FoodMinistryServer(StorageType.INMEMORY);
        server.start(hostAddress, port);
        LoggingAdapter logger = Logging.getLogger(server.getSystem(), FoodMinistryInMemoryLauncher.class);
        logger.info("Server online at http://{}:{}/", hostAddress, port);
    }
}

package org.food.ministry.rest;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FoodMinistryLauncher {
    
    public static void main(String... args) {
        FoodMinistryServer server = new FoodMinistryServer();
        server.start();
        LoggingAdapter logger = Logging.getLogger(server.getSystem(), FoodMinistryLauncher.class);
        logger.info("Server online at http://localhost:8080/");
    }
}

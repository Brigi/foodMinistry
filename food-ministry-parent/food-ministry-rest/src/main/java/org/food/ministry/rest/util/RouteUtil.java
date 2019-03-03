package org.food.ministry.rest.util;

import java.util.function.Function;

import org.food.ministry.rest.FoodMinistryServer;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Route;

public class RouteUtil {

    private RouteUtil() { /* Static class */ }
    
    public static <T> Route createGetRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> getLogic) {
        return server.pathPrefix(path,
                () -> server.get(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), getLogic)));
    }
    
    public static <T> Route createPutRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> putLogic) {
        return server.pathPrefix(path,
                () -> server.put(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), putLogic)));
    }

    public static <T> Route createDeleteRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> deleteLogic) {
        return server.pathPrefix(path,
                () -> server.delete(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), deleteLogic)));
    }
}

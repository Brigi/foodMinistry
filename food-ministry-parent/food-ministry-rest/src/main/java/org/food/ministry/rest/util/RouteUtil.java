package org.food.ministry.rest.util;

import java.util.function.Function;

import org.food.ministry.rest.FoodMinistryServer;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.PathMatcher0;
import akka.http.javadsl.server.PathMatcher1;
import akka.http.javadsl.server.Route;

public class RouteUtil {

    private RouteUtil() { /* Static class */ }
    
    public static <T> Route createGetRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> getLogic) {
        return server.pathPrefix(path,
                () -> server.get(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), getLogic)));
    }
    
    public static <T> Route createGetRoute(FoodMinistryServer server, PathMatcher0 path, Class<T> marshellingClass, Function<T, Route> getLogic) {
        return server.pathPrefix(path, () -> server.get(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), getLogic)));
    }
    
    public static <U,T> Route createGetRoute(FoodMinistryServer server, PathMatcher1<U> path, Class<T> marshellingClass, Function<T, Route> getLogic) {
        return server.pathPrefix(path, (argument) -> server.get(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), getLogic)));
    }
    
    public static <T> Route createPutRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> putLogic) {
        return server.pathPrefix(path,
                () -> server.put(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), putLogic)));
    }
    
    public static <T> Route createPutRoute(FoodMinistryServer server, PathMatcher0 path, Class<T> marshellingClass, Function<T, Route> putLogic) {
        return server.pathPrefix(path, () -> server.put(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), putLogic)));
    }
    
    public static <U,T> Route createPutRoute(FoodMinistryServer server, PathMatcher1<U> path, Class<T> marshellingClass, Function<T, Route> putLogic) {
        return server.pathPrefix(path, (argument) -> server.put(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), putLogic)));
    }

    public static <T> Route createDeleteRoute(FoodMinistryServer server, String path, Class<T> marshellingClass, Function<T, Route> deleteLogic) {
        return server.pathPrefix(path,
                () -> server.delete(
                        () -> server.entity(Jackson.unmarshaller(marshellingClass), deleteLogic)));
    }
    
    public static <T> Route createDeleteRoute(FoodMinistryServer server, PathMatcher0 path, Class<T> marshellingClass, Function<T, Route> deleteLogic) {
        return server.pathPrefix(path, () -> server.delete(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), deleteLogic)));
    }
    
    public static <U,T> Route createDeleteRoute(FoodMinistryServer server, PathMatcher1<U> path, Class<T> marshellingClass, Function<T, Route> deleteLogic) {
        return server.pathPrefix(path, (argument) -> server.delete(
                () -> server.entity(Jackson.unmarshaller(marshellingClass), deleteLogic)));
    }
}

package org.food.ministry.rest.util;

import java.util.function.Function;
import java.util.function.Supplier;

import org.food.ministry.rest.FoodMinistryServer;

import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;

public class RouteUtil {

    public static Route createGetRoute(FoodMinistryServer server, String path, Supplier<Route> route) {
        return server.path(path, () -> server.get(route));
    }

    public static Route createGetRouteWithPrefix(FoodMinistryServer server, String prefix, Function<Long, Route> route) {
        return server.pathPrefix(prefix, () -> server.path(PathMatchers.longSegment(), (Long id) -> server.get(() -> route.apply(id))));
    }

    public static Route createPostRoute(FoodMinistryServer server, String path, Supplier<Route> route) {
        return server.path(path, () -> server.post(route));
    }

    public static Route createPutRoute(FoodMinistryServer server, String path, Supplier<Route> route) {
        return server.path(path, () -> server.put(route));
    }

    public static Route createDeleteRoute(FoodMinistryServer server, String path, Supplier<Route> route) {
        return server.path(path, () -> server.delete(route));
    }
}

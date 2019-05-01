package org.food.ministry.rest;

import akka.http.javadsl.server.Route;

public interface IEndpoint {

    Route getRoute();
}

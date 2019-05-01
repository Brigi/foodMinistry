package org.food.ministry.rest.util;

import org.food.ministry.rest.FoodMinistryServer;
import org.food.ministry.rest.json.ErrorJSON;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.directives.RouteAdapter;

public class MessageUtil {

    public static RouteAdapter createErrorResponse(FoodMinistryServer server, String errorMessage) {
        return server.complete(StatusCodes.BAD_REQUEST, new ErrorJSON(errorMessage), Jackson.<ErrorJSON>marshaller());
    }
}

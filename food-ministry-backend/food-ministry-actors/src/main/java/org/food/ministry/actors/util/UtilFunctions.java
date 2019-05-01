package org.food.ministry.actors.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.exceptions.DataAccessException;

import akka.event.LoggingAdapter;

/**
 * This static class contains helper methods for common usage
 * 
 * @author Maximilian Briglmeier
 * @since 17.02.2019
 */
public final class UtilFunctions {

    private UtilFunctions() {
        /* No constructor needed as this a static class only */}

    /**
     * Gets the stack trace of the given {@link Throwable} as {@link String}
     * 
     * @param throwable The throwable object to get the stack trace from
     * @return The stack trace in String format
     */
    public static String getStacktraceAsString(Throwable throwable) {
        StringWriter stringWrite = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWrite);
        throwable.printStackTrace(printWriter);

        return stringWrite.toString();
    }

    /**
     * Generates a unique ID by asking the provided {@link DAO} if the random
     * generated id already exists. As this is highly unlikely due to the high
     * entropy this method most likely returns without the need to generate a second
     * ID.
     * 
     * @param <T> The model type of the data access object to be persisted
     * @param dao The {@link DAO}, which checks the uniqueness of the generated IDs
     * @param logger The logger used for logging the rare situation when a ID
     *            collision occurs
     * @return A unique id for the given data access object
     * @throws DataAccessException Thrown when the underlying data source is
     *             unavailable or corrupted
     */
    public static <T> long generateUniqueId(DAO<T> dao, LoggingAdapter logger) throws DataAccessException {
        long id = 0;
        boolean uniqueIdFound = false;
        while(!uniqueIdFound) {
            id = IDGenerator.getRandomID();
            if(!dao.doesIdExist(id)) {
                uniqueIdFound = true;
            } else {
                logger.info("WOW! Rare instance occoured where an already ID was generated. ID was {}", id);
            }
        }

        return id;
    }
}

package org.food.ministry.data.access.exceptions;

/**
 * This class represents an exception, which gets thrown once a CRUD operation
 * fails.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class DataAccessException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor without any additional information
     */
    public DataAccessException() {
        super();
    }

    /**
     * Constructor accepting an additional error message
     * 
     * @param message
     *            The additional error message
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructor accepting a throwable, which was the actual root cause of this
     * exception
     * 
     * @param throwable
     *            The actual root cause
     */
    public DataAccessException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor accepting an additional error message and a throwable, which was
     * the actual root cause of this exception
     * 
     * @param message
     *            The additional error message
     * @param throwable
     *            The actual root cause
     */
    public DataAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

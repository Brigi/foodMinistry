package org.food.ministry.model.exception;

/**
 * This class represents an exception, which gets thrown once a requested
 * ingredient was not found.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class RecipeNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3953680614970994371L;

    /**
     * Default constructor without any additional information
     */
    public RecipeNotFoundException() {
        super();
    }

    /**
     * Constructor accepting an additional error message
     * 
     * @param message
     *            The additional error message
     */
    public RecipeNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor accepting a throwable, which was the actual root cause of this
     * exception
     * 
     * @param throwable
     *            The actual root cause
     */
    public RecipeNotFoundException(Throwable throwable) {
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
    public RecipeNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

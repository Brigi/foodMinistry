package org.food.ministry.model.exception;

/**
 * This class represents an exception, which gets thrown once a requested
 * ingredient was not found.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class IngredientNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3953680614970994371L;

    /**
     * Default constructor without any additional information
     */
    public IngredientNotFoundException() {
        super();
    }

    /**
     * Constructor accepting an additional error message
     * 
     * @param message
     *            The additional error message
     */
    public IngredientNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor accepting a throwable, which was the actual root cause of this
     * exception
     * 
     * @param throwable
     *            The actual root cause
     */
    public IngredientNotFoundException(Throwable throwable) {
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
    public IngredientNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

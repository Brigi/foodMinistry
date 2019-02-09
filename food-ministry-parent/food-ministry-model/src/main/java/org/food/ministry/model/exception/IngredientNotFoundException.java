package org.food.ministry.model.exception;

public class IngredientNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3953680614970994371L;

    public IngredientNotFoundException() {
        super();
    }
    
    public IngredientNotFoundException(String message) {
        super(message);
    }
    
    public IngredientNotFoundException(Throwable throwable) {
        super(throwable);
    }
    
    public IngredientNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

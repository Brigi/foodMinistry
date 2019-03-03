package org.food.ministry.rest.json;

public class ErrorJSON {

    private String errorMessage;
    
    public ErrorJSON() { }
    
    public ErrorJSON(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

package org.food.ministry.model;

public enum Unit {
    KILOGRAMM("kg"),
    GRAMM("g"),
    MILILITER("ml"),
    LITER("l"),
    TABLESPOON("tbsp"),
    TEASPOON("tsp."),
    NONE("");
    
    private String representation;
    
    private Unit(String representation) {
        this.representation = representation;
    }
    
    public String toString() {
        return this.representation;
    }
}

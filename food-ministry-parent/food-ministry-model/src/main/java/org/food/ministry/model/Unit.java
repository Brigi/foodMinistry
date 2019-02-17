package org.food.ministry.model;

/**
 * This enum represents the possible units in which {@link Ingredient}s can be
 * measured.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public enum Unit {
    KILOGRAMM("kg"), GRAMM("g"), MILILITER("ml"), LITER("l"), TABLESPOON("tbsp."), TEASPOON("tsp."), NONE("");

    /**
     * The textual representation of the unit
     */
    private String representation;

    /**
     * Constructor setting the textual representation of the unit
     * 
     * @param representation
     */
    private Unit(String representation) {
        this.representation = representation;
    }

    /**
     * Gets the textual representation of the unit
     * 
     * @return The textual representation of the unit
     */
    public String toString() {
        return this.representation;
    }
}

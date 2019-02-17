package org.food.ministry.model;

import java.text.MessageFormat;

/**
 * This class represents one ingredient. It is the basic class for all
 * operations and is used in recipes, shopping lists and food inventories. The
 * creation of ingredients should be unique, which is handled by the
 * {@link IngredientsPool} class.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class Ingredient extends PersistenceObject {

    /**
     * The name of the ingredient
     */
    private String name;

    /**
     * The unit in which this ingredient gets measured
     */
    private Unit unit;

    /**
     * Determines, if this ingredient is a basic ingredient
     */
    private boolean isBasic;

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id
     *            The unique id of this ingredient
     * @param name
     *            The name of this ingredient
     * @param unit
     *            The unit in which this ingredient gets measured
     * @param isBasic
     *            Determines, if this ingredient is a basic ingredient. Basic
     *            ingredients don't get added to the shopping list if needed by a
     *            recipe.
     */
    public Ingredient(long id, String name, Unit unit, boolean isBasic) {
        super(id);
        this.name = name;
        this.unit = unit;
        this.isBasic = isBasic;
    }

    /**
     * Gets the name of this ingredient
     * 
     * @return The name of this ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this ingredient
     * 
     * @param name
     *            The name of this ingredient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unit in which this ingredient gets measured
     * 
     * @return The unit in which this ingredient gets measured
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the unit in which this ingredient gets measured
     * 
     * @param unit
     *            The unit in which this ingredient gets measured
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Determines, if this ingredient is a basic ingredient.
     * 
     * @return True, if this ingredient is a basic ingredient; else false
     */
    public boolean isBasic() {
        return isBasic;
    }

    /**
     * Sets this ingredient to be a basic ingredient or not depending on the passed
     * value
     * 
     * @param isBasic
     *            If true, this ingredient is handled as a basic ingredient; else
     *            this ingredient is handled as a non basic ingredient
     */
    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }

    /**
     * Returns the String representation of this ingredient, which contains the
     * name, unit and if it is a basic ingredient. <br>
     * E.g.: <i>"Name: Potato, Unit: kg, isBasic: false"</i>
     */
    public String toString() {
        return MessageFormat.format("Name: {0}, Unit: {1}, isBasic: {2}", this.name, this.unit, this.isBasic);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isBasic ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        Ingredient other = (Ingredient) obj;
        if(isBasic != other.isBasic)
            return false;
        if(name == null) {
            if(other.name != null)
                return false;
        } else if(!name.equals(other.name))
            return false;
        if(unit != other.unit)
            return false;
        return true;
    }
}

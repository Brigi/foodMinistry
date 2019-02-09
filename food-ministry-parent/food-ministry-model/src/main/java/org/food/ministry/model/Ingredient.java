package org.food.ministry.model;

import java.text.MessageFormat;

public class Ingredient {

    private String name;

    private Unit unit;

    private boolean isBasic;

    public Ingredient(String name, Unit unit, boolean isBasic) {
        this.name = name;
        this.unit = unit;
        this.isBasic = isBasic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isBasic() {
        return isBasic;
    }

    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }

    public String toString() {
        return MessageFormat.format("Name: {0}, Unit: {1}, isBasic: {2}", this.name, this.unit, this.isBasic);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isBasic ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ingredient other = (Ingredient) obj;
        if (isBasic != other.isBasic)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (unit != other.unit)
            return false;
        return true;
    }    
}

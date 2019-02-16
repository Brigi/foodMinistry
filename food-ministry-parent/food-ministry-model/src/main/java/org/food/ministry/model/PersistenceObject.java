package org.food.ministry.model;

public abstract class PersistenceObject {

    /**
     * The unique id of the user
     */
    private long id;
    
    /**
     * Constructor setting the unique id of a persisted object
     * @param id A unique id for the kind of object
     */
    public PersistenceObject(long id) {
        this.id = id;
    }
    
    /**
     * Gets the id of the persisted object
     * 
     * @return The id of the persisted object
     */
    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        PersistenceObject other = (PersistenceObject) obj;
        if (id != other.id)
            return false;
        return true;
    }
}

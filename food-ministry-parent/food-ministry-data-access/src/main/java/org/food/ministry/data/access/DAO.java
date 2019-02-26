package org.food.ministry.data.access;

import java.util.Collection;

import org.food.ministry.data.access.exceptions.DataAccessException;

public interface DAO<T> {

    /**
     * Generic message for indicating that no item was found for a given id
     */
    public static final String NO_ID_FOUND_MESSAGE = "Item with id {0} not found";

    /**
     * Generic message for indicating that none unique item was found
     */
    public static final String INSUFFICIENT_AMOUNT_MESSAGE = "Insufficient amount of items found: {0}";
    
    T get(long id) throws DataAccessException;

    Collection<T> getAll() throws DataAccessException;

    void save(T item) throws DataAccessException;

    void update(T item) throws DataAccessException;

    void delete(T item) throws DataAccessException;

    boolean doesIdExist(long id) throws DataAccessException;
}

package org.food.ministry.data.access;

import java.util.Collection;

import org.food.ministry.data.access.exceptions.DataAccessException;

public interface DAO<T> {

    T get(long id) throws DataAccessException;

    Collection<T> getAll() throws DataAccessException;

    void save(T t) throws DataAccessException;

    void update(T ts) throws DataAccessException;

    void delete(T t) throws DataAccessException;

    boolean doesIdExist(long id) throws DataAccessException;
}

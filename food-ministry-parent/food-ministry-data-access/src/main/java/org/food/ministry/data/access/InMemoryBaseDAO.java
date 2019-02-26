package org.food.ministry.data.access;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.PersistenceObject;

public abstract class InMemoryBaseDAO<T extends PersistenceObject> implements DAO<T> {

    private Map<Long, T> items = new HashMap<>();
    
    protected Map<Long, T> getItems() {
        return this.items;
    }
    
    @Override
    public T get(long id) throws DataAccessException {
        if(!items.containsKey(id)) {
            throw new DataAccessException(MessageFormat.format(NO_ID_FOUND_MESSAGE, id));
        }
        return items.get(id);
    }

    @Override
    public Collection<T> getAll() throws DataAccessException {
        return items.values();
    }

    @Override
    public void save(T item) throws DataAccessException {
        items.put(item.getId(), item);
    }
    
    protected T getItemToUpdate(T item) throws DataAccessException {
        List<T> foundItems = items.values().parallelStream().filter(element -> element.getId() == item.getId()).collect(Collectors.toList());
        if(foundItems.size() != 1) {
            throw new DataAccessException(MessageFormat.format(INSUFFICIENT_AMOUNT_MESSAGE, foundItems.size()));
        }
        return foundItems.get(0);
    }

    @Override
    public void delete(T item) throws DataAccessException {
        List<Long> ids = items.entrySet().parallelStream().filter(element -> element.getValue().equals(item)).map(element -> element.getKey())
                .collect(Collectors.toList());
        if(ids.size() == 0) {
            throw new DataAccessException(MessageFormat.format(NO_ID_FOUND_MESSAGE, item.getId()));
        }
        items.remove(ids.get(0));
    }

    @Override
    public boolean doesIdExist(long id) throws DataAccessException {
        return items.containsKey(id);
    }

}

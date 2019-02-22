package org.food.ministry.data.access.household;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;

public class InMemoryHouseholdDAO implements HouseholdDAO {

    private Map<Long, Household> households = new HashMap<>();

    @Override
    public Household get(long id) throws DataAccessException {
        if(!households.containsKey(id)) {
            throw new DataAccessException(MessageFormat.format(NO_ID_FOUND_MESSAGE, id));
        }
        return households.get(id);
    }

    @Override
    public Collection<Household> getAll() throws DataAccessException {
        return households.values();
    }

    @Override
    public void save(Household household) throws DataAccessException {
        households.put(household.getId(), household);
    }

    @Override
    public void update(Household household) throws DataAccessException {
        List<Household> foundHouseholds = households.values().parallelStream().filter(element -> element.getId() == household.getId()).collect(Collectors.toList());
        if(foundHouseholds.size() != 1) {
            throw new DataAccessException(MessageFormat.format(INSUFFICIENT_AMOUNT_MESSAGE, foundHouseholds.size()));
        }
        Household householdToUpdate = foundHouseholds.get(0);
        householdToUpdate.setName(household.getName());
        householdToUpdate.setIngredientsPool(household.getIngredientsPool());
    }

    @Override
    public void delete(Household household) throws DataAccessException {
        List<Long> ids = households.entrySet().parallelStream().filter(element -> element.getValue().equals(household)).map(element -> element.getKey())
                .collect(Collectors.toList());
        if(ids.size() == 0) {
            throw new DataAccessException(MessageFormat.format(NO_HOUSEHOLD_FOUND_MESSAGE, household.getName()));
        }
        households.remove(ids.get(0));
    }

    @Override
    public boolean doesIdExist(long id) throws DataAccessException {
        return households.containsKey(id);
    }
}

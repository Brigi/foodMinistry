package org.food.ministry.data.access.household;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;

public class InMemoryHousehold implements HouseholdDAO {

    private Map<Long, Household> households = new HashMap<>();
    
    @Override
    public Household get(long id) throws DataAccessException {
        return households.get(id);
    }

    @Override
    public Collection<Household> getAll() throws DataAccessException {
        return households.values();
    }

    @Override
    public void save(Household household) throws DataAccessException {
        households.put(Long.valueOf(household.hashCode()), household);
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
        // TODO Auto-generated method stub
        
    }
 }

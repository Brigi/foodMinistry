package org.food.ministry.data.access.household;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;

public class InMemoryHouseholdDAO extends InMemoryBaseDAO<Household> implements HouseholdDAO {

    @Override
    public void update(Household household) throws DataAccessException {
        Household householdToUpdate = getItemToUpdate(household);
        householdToUpdate.setName(household.getName());
        householdToUpdate.setIngredientsPool(household.getIngredientsPool());
    }
}

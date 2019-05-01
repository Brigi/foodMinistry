package org.food.ministry.data.access.household;

public class TestInMemoryHouseholdDAO extends TestHouseholdDAO {

    @Override
    protected HouseholdDAO getHouseholdDao() {
        return new InMemoryHouseholdDAO();
    }
}

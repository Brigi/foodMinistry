package org.food.ministry.data.access;

import org.food.ministry.data.access.household.TestInMemoryHouseholdDAO;
import org.food.ministry.data.access.users.TestInMemoryUserDAO;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestInMemoryUserDAO.class, TestInMemoryHouseholdDAO.class })

public class AllTests {

}

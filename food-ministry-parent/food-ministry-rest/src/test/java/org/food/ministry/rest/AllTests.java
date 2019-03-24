package org.food.ministry.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestShutdownEndpoint.class, TestUserEndpoint.class, TestHouseholdEndpoint.class, TestIngredientsPoolEndpoint.class, TestFoodInventoryEndpoint.class, TestRecipesPoolEndpoint.class })
public class AllTests {

}

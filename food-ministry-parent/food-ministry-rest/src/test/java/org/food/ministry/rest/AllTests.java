package org.food.ministry.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestShutdownEndpoint.class, TestUserEndpoint.class })
public class AllTests {

}

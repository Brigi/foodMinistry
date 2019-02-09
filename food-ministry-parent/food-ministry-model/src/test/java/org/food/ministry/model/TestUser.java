package org.food.ministry.model;

import org.junit.Assert;
import org.junit.Test;

public class TestUser {

    @Test
    public void testGetNameAndEmailAddress() {
        final String name = "My Name";
        final String email = "name@provider.com";
        User user = new User(email, name);
        Assert.assertEquals(name, user.getName());
        Assert.assertEquals(email, user.getEmailAddress());
    }
    
    @Test
    public void testSetNameAndEmailAddress() {
        final String name = "My Name";
        final String email = "name@provider.com";
        User user = new User("some@mail.com", "Some Name");
        user.setName(name);
        user.setEmailAddress(email);
        Assert.assertEquals(name, user.getName());
        Assert.assertEquals(email, user.getEmailAddress());
    }
    
    @Test
    public void testAddHousehold() {
        User user = new User("My Name", "name@provider.com");
        Household household = new Household("My Household");
        user.addHousehold(household);
        Assert.assertEquals(1, user.getHouseholds().size());
    }
    
    @Test
    public void testRemoveHousehold() {
        User user = new User("My Name", "name@provider.com");
        Household household = new Household("My Household");
        user.addHousehold(household);
        user.removeHousehold(household);
        Assert.assertEquals(0, user.getHouseholds().size());
    }
}

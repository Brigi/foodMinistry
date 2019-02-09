package org.food.ministry.model;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String emailAddress;
    
    private String name;
    
    private Set<Household> households;
    
    public User(String emailAddress, String name) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.households = new HashSet<Household>();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Household> getHouseholds() {
        return this.households;
    }
    
    public void addHousehold(Household household) {
        this.households.add(household);
    }
    
    public void removeHousehold(Household household) {
        this.households.remove(household);
    }
}

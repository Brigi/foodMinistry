package org.food.ministry.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a user of the application. Each user can contain several {@link Household}s, which he/she manages and can apply actions on.
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class User {

    /**
     * The email address of the user
     */
    private String emailAddress;
    
    /**
     * The name of the user
     */
    private String name;
    
    /**
     * The households of the user
     */
    private Set<Household> households;
    
    /**
     * Constructor setting essential member variables of the user
     * @param emailAddress The email address of the user
     * @param name The name of the user
     */
    public User(String emailAddress, String name) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.households = new HashSet<Household>();
    }

    /**
     * Gets the email address of the user
     * @return The email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user
     * @param emailAddress The email address of the user
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the name of the user
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user
     * @param name The name of the user
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets a set of households the user manages
     * @return A set of households
     */
    public Set<Household> getHouseholds() {
        return this.households;
    }
    
    /**
     * Adds a household to the set households of this user to manage
     * @param household The household to add
     */
    public void addHousehold(Household household) {
        this.households.add(household);
    }
    
    /**
     * Removes the given household from the set of households the user manages
     * @param household The household to remove
     */
    public void removeHousehold(Household household) {
        this.households.remove(household);
    }
}

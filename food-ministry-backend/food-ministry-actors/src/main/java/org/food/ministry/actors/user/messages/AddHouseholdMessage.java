package org.food.ministry.actors.user.messages;

import org.food.ministry.actors.messages.AMessage;
import org.food.ministry.actors.messages.IRequestMessage;

/**
 * The message for adding a household to a user.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class AddHouseholdMessage extends AMessage implements IRequestMessage {

    /**
     * The id of the user
     */
    private final long userId;

    /**
     * The name of the household
     */
    private final String name;

    /**
     * Constructor initializing essential member variables
     * 
     * @param id The id of the message
     * @param userId The id of the user
     * @param name The name of the household
     */
    public AddHouseholdMessage(long id, long userId, String name) {
        super(id);
        this.userId = userId;
        this.name = name;
    }

    /**
     * Gets the id of the user
     * 
     * @return The id of the user
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Gets the name of the household
     * 
     * @return The name of the household
     */
    public String getName() {
        return name;
    }
}

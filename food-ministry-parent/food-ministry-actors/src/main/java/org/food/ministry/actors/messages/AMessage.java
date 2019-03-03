package org.food.ministry.actors.messages;

public class AMessage {

    /**
     * The ID of this message
     */
    private final long id;

    /**
     * Constructor initializing the essential member variables
     * 
     * @param id The ID of the message
     */
    public AMessage(long id) {
        this.id = id;
    }

    /**
     * Gets the ID of this message
     * 
     * @return The ID of this message
     */
    public long getId() {
        return id;
    }
}

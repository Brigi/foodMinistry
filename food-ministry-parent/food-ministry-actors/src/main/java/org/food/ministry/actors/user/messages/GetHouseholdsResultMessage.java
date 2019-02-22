package org.food.ministry.actors.user.messages;

import java.util.Map;

import org.food.ministry.actors.messages.AResultMessage;

/**
 * The message for transmitting the result of requested households.
 * 
 * @author Maximilian Briglmeier
 * @since 22.02.2019
 */
public class GetHouseholdsResultMessage extends AResultMessage {

    /**
     * A map containing the ids of the household and their corresponding name
     */
    private final Map<Long, String> households;

    /**
     * The constructor initializing essential member variables
     * 
     * @param id The ID of the message
     * @param originId The ID of the requesting message
     * @param successful Determines if the requesting message was successfully
     *            processed
     * @param errorMessage Error message in case something went wrong
     * @param households A map containing the ids of the households and their
     *            corresponding name
     */
    public GetHouseholdsResultMessage(long id, long originId, boolean successful, String errorMessage, Map<Long, String> households) {
        super(id, originId, successful, errorMessage);
        this.households = households;
    }

    /**
     * Gets the map containing the ids of the households and their corresponding
     * name
     * 
     * @return A map containing the ids of the households and their corresponding
     *         names
     */
    public Map<Long, String> getHouseholdIdsWithName() {
        return households;
    }
}

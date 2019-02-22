package org.food.ministry.data.access.household;

import org.food.ministry.data.access.DAO;
import org.food.ministry.model.Household;

/**
 * Data access object for handling {@link Household} objects from a storage.
 * 
 * @author Maximilian Briglmeier
 * @since 21.02.2019
 */
public interface HouseholdDAO extends DAO<Household> {

    /**
     * Generic message for indicating that no household was found for a given id
     */
    public static final String NO_ID_FOUND_MESSAGE = "Household for id {0} not found";

    /**
     * Generic message for indicating that no household was found for a given name
     */
    public static final String NO_HOUSEHOLD_FOUND_MESSAGE = "Household {0} not found";

    /**
     * Generic message for indicating that none unique household was found
     */
    public static final String INSUFFICIENT_AMOUNT_MESSAGE = "Insufficient amount of households found: {0}";
}

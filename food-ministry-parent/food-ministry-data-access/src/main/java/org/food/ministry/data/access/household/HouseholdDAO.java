package org.food.ministry.data.access.household;

import org.food.ministry.data.access.DAO;
import org.food.ministry.model.Household;

public interface HouseholdDAO extends DAO<Household> {
    
    public static final String NO_ID_FOUND_MESSAGE = "Household for id {0} not found";
    
    public static final String NO_HOUSEHOLD_FOUND_MESSAGE = "Household {0} not found";

    public static final String INSUFFICIENT_AMOUNT_MESSAGE = "Insufficient amount of households found: {0}";
}

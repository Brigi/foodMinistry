package org.food.ministry.model;

/**
 * A special kind of {@link AFoodStorage}, which represents the current combined
 * food supply of the user.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class FoodInventory extends AFoodStorage {

    /**
     * Constructor setting essential member variables
     * 
     * @param id
     *            The unique id of this food storage
     */
    public FoodInventory(long id) {
        super(id);
    }
}

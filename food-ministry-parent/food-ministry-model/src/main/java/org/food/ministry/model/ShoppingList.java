package org.food.ministry.model;

/**
 * A special kind of {@link AFoodStorage}, which represents the
 * {@link Ingredient}s needed for the user to cook specific {@link Recipe}s in
 * the future.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class ShoppingList extends AFoodStorage {

    /**
     * Constructor setting essential member variables
     * 
     * @param id The unique id of this shopping list
     */
    public ShoppingList(long id) {
        super(id);
    }
}

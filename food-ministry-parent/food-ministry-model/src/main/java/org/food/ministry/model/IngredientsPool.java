package org.food.ministry.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.food.ministry.model.exception.IngredientNotFoundException;

/**
 * This class represents all available {@link Ingredient}s and serves as an
 * entity to retrieve templates of {@link Ingredient}s. Hence each
 * {@link Ingredient} in this pool is unique.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class IngredientsPool extends PersistenceObject {

    /**
     * A set of all available ingredients
     */
    private Set<Ingredient> ingredients;

    /**
     * Default constructor initializing member variables
     * 
     * @param id The unique id of this ingredients pool
     */
    public IngredientsPool(long id) {
        super(id);
        this.ingredients = new HashSet<Ingredient>();
    }

    /**
     * Gets an unmodifiable set of current ingredients in this pool
     * 
     * @return An unmodifiable set of current ingredients in this pool
     */
    public Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(this.ingredients);
    }
    
    /**
     * Sets an unmodifiable set of current ingredients in this pool
     * 
     * @param ingredients The ingredients to set for this pool
     */
    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the desired ingredient from the pool by name.
     * 
     * @param name The name of the ingredient to get
     * @return The desired ingredient, if found
     * @throws IngredientNotFoundException Thrown if there exists no ingredient in
     *             this pool with the given name
     */
    public Ingredient getIngredient(String name) throws IngredientNotFoundException {
        if(name == null) {
            throw new IllegalArgumentException("Can't search for ingredient given as 'null'");
        }
        for(Ingredient ingredient: ingredients) {
            if(name.equals(ingredient.getName())) {
                return ingredient;
            }
        }

        throw new IngredientNotFoundException("The ingredient '" + name + "' was not found!");
    }

    /**
     * Adds the given ingredient to this pool. If the ingredient is already present
     * in this pool, nothing happens.
     * 
     * @param ingredient The ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    /**
     * Removes the given ingredient from the pool. If the ingredient did not exist,
     * nothing happens.
     * 
     * @param ingredient The ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }
}

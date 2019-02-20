package org.food.ministry.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.food.ministry.model.exception.RecipeNotFoundException;

/**
 * This class contains all available {@link Recipe}s of one {@link Household} and serves as an
 * collection to retrieve single {@link Recipe}s. 
 * 
 * @author Maximilian Briglmeier
 * @since 20.02.2019
 *
 */
public class RecipesPool extends PersistenceObject {

    /**
     * A set of all available recipes
     */
    private Set<Recipe> recipes;

    /**
     * Default constructor initializing member variables
     * 
     * @param id
     *            The unique id of this recipes pool
     */
    public RecipesPool(long id) {
        super(id);
        this.recipes = new HashSet<>();
    }

    /**
     * Gets an unmodifiable set of current recipes in this pool
     * 
     * @return An unmodifiable set of current recipes in this pool
     */
    public Set<Recipe> getRecipes() {
        return Collections.unmodifiableSet(this.recipes);
    }

    /**
     * Gets the desired recipe from the pool by name.
     * 
     * @param name
     *            The name of the recipe to get
     * @return The desired recipe, if found
     * @throws RecipeNotFoundException
     *             Thrown if there exists no recipe in this pool with the given
     *             name
     */
    public Recipe getRecipe(String name) throws RecipeNotFoundException {
        if(name == null) {
            throw new IllegalArgumentException("Can't search for recipe given as 'null'");
        }
        for(Recipe recipe: recipes) {
            if(name.equals(recipe.getName())) {
                return recipe;
            }
        }

        throw new RecipeNotFoundException("The recipe '" + name + "' was not found!");
    }

    /**
     * Adds the given recipe to this pool. If the recipe is already present
     * in this pool, nothing happens.
     * 
     * @param recipe
     *            The recipe to add
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Removes the given recipe from the pool. If the recipe did not exist,
     * nothing happens.
     * 
     * @param recipe
     *            The recipe to remove
     */
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }
}

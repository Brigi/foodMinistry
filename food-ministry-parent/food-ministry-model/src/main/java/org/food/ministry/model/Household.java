package org.food.ministry.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the household of the user. It contains all elements
 * needed for this application to work and serves as entry point to apply
 * actions on the different entities the user got. This actions can for example
 * be adding supply to the food storage, cooking recipes, adding ingredients to
 * the shopping list and etc..
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public class Household extends PersistenceObject {

    /**
     * The name of the household. This might be needed to differ between several
     * different households the user might have.
     */
    private String name;

    /**
     * The food inventory of this household
     */
    private FoodInventory foodInventory;

    /**
     * The shopping list of this household
     */
    private ShoppingList shoppingList;

    /**
     * The available ingredients of this household
     */
    private IngredientsPool ingredientsPool;

    /**
     * The recipes of this household
     */
    private Set<Recipe> recipes;

    /**
     * Constructor initializing this class. It accepts a name to which this
     * household should be associated.
     * 
     * @param name
     *            The name of this household
     */
    public Household(long id, FoodInventory foodInventory, ShoppingList shoppingList, IngredientsPool ingredientsPool, String name) {
        super(id);
        this.name = name;
        this.foodInventory = foodInventory;
        this.shoppingList = shoppingList;
        this.ingredientsPool = ingredientsPool;
        this.recipes = new HashSet<>();
    }

    /**
     * Gets the ingredients pool of this household
     * 
     * @return The ingredients pool of this household
     */
    public IngredientsPool getIngredientsPool() {
        return ingredientsPool;
    }

    /**
     * Sets the ingredients pool of this household
     * 
     * @param ingredientsPool
     *            The ingredients pool to set
     */
    public void setIngredientsPool(IngredientsPool ingredientsPool) {
        this.ingredientsPool = ingredientsPool;
    }

    /**
     * Gets the name of this household
     * 
     * @return The name of this household
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this household
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a set of recipes available in this household
     * 
     * @return A set of recipes available in this household
     */
    public Set<Recipe> getRecipes() {
        return Collections.unmodifiableSet(recipes);
    }

    /**
     * Adds the given recipes to the set of current recipes available in this
     * household
     * 
     * @param recipes
     *            The set of recipes to add
     */
    public void addRecipes(Set<Recipe> recipes) {
        this.recipes.addAll(recipes);
    }

    /**
     * Adds the given recipe to the set of current recipes available in this
     * household
     * 
     * @param recipe
     *            The recipe to add
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Removes the given recipes from the current set of recipes available in this
     * household
     * 
     * @param recipe
     *            The recipe to remove
     */
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }

    /**
     * Gets the food inventory of this household
     * 
     * @return The food inventory of this household
     */
    public FoodInventory getFoodInventory() {
        return foodInventory;
    }

    /**
     * Gets the shopping list of this household
     * 
     * @return The shopping list of this household
     */
    public ShoppingList getShoppingList() {
        return shoppingList;
    }
}

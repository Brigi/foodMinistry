package org.food.ministry.model;

import java.util.Map;

/**
 * This class represent a recipe of the user. It contains a list of
 * {@link Ingredient}s and the needed quantity needed to cook this recipe.
 * Additionally an optional description can be given.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 */
public class Recipe extends PersistenceObject {

    /**
     * The name of the recipe
     */
    private String name;

    /**
     * The ingredients needed for this recipe with its associated quantity
     */
    private Map<Ingredient, Float> ingredients;

    /**
     * The description for this recipe
     */
    private String description;

    /**
     * Constructor initializing the essential elements of a recipe
     * 
     * @param id The unique id of the recipe
     * @param name The name of the recipe
     * @param ingredients A map containing the ingredients needed associated with
     *            the quantity
     * @param description A description of this recipe
     */
    public Recipe(long id, String name, Map<Ingredient, Float> ingredients, String description) {
        super(id);
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
    }

    /**
     * Gets the name of this recipe
     * 
     * @return The name of this recipe
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this recipe
     * 
     * @param name The name of this recipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a map containing the needed ingredients for this recipe associated with
     * the quantity
     * 
     * @return A map containing the needed ingredients for this recipe associated
     *         with the quantity
     */
    public Map<Ingredient, Float> getIngredientsWithQuantity() {
        return ingredients;
    }

    /**
     * Sets a map containing the needed ingredients for this recipe associated with
     * the quantity
     * 
     * @param ingredients A map containing the needed ingredients for this recipe
     *            associated with the quantity
     */
    public void setIngredientsWithQuantity(Map<Ingredient, Float> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the description of this recipe
     * 
     * @return The description of this recipe
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this recipe
     * 
     * @param description The description of this recipe
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

package org.food.ministry.model;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.food.ministry.model.util.Util;

/**
 * This is an abstract class representing any storage of food. It contains
 * {@link Ingredient}s with a certain quantity, which can be altered by any
 * action the user takes.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public abstract class AFoodStorage extends PersistenceObject {

    /**
     * A map containing {@link Ingredient}s with their quantity
     */
    private Map<Ingredient, Float> ingredients;

    /**
     * Default constructor initializing member variables
     * 
     * @param id
     *            The unique if of this food storage
     */
    public AFoodStorage(long id) {
        super(id);
        this.ingredients = new HashMap<>();
    }

    /**
     * Gets a map of {@link Ingredient}s with their associated quantity
     * 
     * @return A map of {@link Ingredient}s with their associated quantity
     */
    public Map<Ingredient, Float> getIngredientsWithQuantity() {
        return this.ingredients;
    }

    /**
     * Adds the given {@link Ingredient} with the given quantity to this food
     * storage. A negative quantity will reduce the current quantity by the amount.
     * Once the quantity of an {@link Ingredient} reaches 0 or less the
     * {@link Ingredient} will be removed.
     * 
     * @param ingredient
     *            The ingredient to add/remove
     * @param quantity
     *            The quantity to add/remove
     */
    public void addIngredient(Ingredient ingredient, float quantity) {
        Util.addIngredientToMap(this.ingredients, ingredient, quantity);
    }

    /**
     * Returns a String with all contained {@link Ingredient}s and their quantity
     * plus the associated Unit to that {@link Ingredient}. The list of
     * {@link Ingredient} is sorted by name. <br>
     * E.g.: <i>"Tomato: 1<br>
     * Sugar: 2 kg<br>
     * Salt: 1 tsp."</i>
     * 
     * @return a String with all contained {@link Ingredient}s and their quantity
     *         plus the associated Unit to that {@link Ingredient}.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        TreeMap<Ingredient, Float> sortedMap = new TreeMap<>(getIngredientComparator());
        sortedMap.putAll(ingredients);
        sortedMap.forEach((ingredient, quantity) -> stringBuilder.append(MessageFormat.format("{0}: {1} {2}\r\n", ingredient.getName(), quantity, ingredient.getUnit())));

        return stringBuilder.toString();
    }

    /**
     * Retursn a comparator, which sorts {@link Ingredient}s by name
     * 
     * @return a comparator, which sorts {@link Ingredient}s by name
     */
    private Comparator<Ingredient> getIngredientComparator() {
        return new Comparator<Ingredient>() {

            @Override
            public int compare(Ingredient o1, Ingredient o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                if(name1 != null) {
                    return name1.compareTo(name2);
                }
                return -1;
            }

        };
    }
}

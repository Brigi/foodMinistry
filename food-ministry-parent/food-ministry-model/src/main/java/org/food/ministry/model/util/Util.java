package org.food.ministry.model.util;

import java.util.Map;

import org.food.ministry.model.Ingredient;

/**
 * This class contains helper functions, which can be called in other classes.
 * 
 * @author Maximilian Briglmeier
 * @since 16.02.2019
 *
 */
public final class Util {

    private Util() {
        /* No constructor needed as this a static class only */}

    /**
     * Adds the given ingredient with the given quantity to the given map. If the
     * ingredient is already present, then the quantity gets added to the already
     * existing quantity in the map. Is the quantity reduced to 0 or below, then the
     * ingredient will be removed from the given map.
     * 
     * @param map
     *            A map containing ingredients with their quantity. This map get
     *            altered in this method
     * @param ingredient
     *            The ingredient to add
     * @param quantity
     *            The quantity to add
     */
    public static void addIngredientToMap(Map<Ingredient, Float> map, Ingredient ingredient, float quantity) {
        if(map.containsKey(ingredient)) {
            float currentQuantity = map.get(ingredient);
            float newQuantity = currentQuantity + quantity;
            if(newQuantity > 0) {
                map.put(ingredient, newQuantity);
            } else {
                map.remove(ingredient);
            }
        } else {
            map.put(ingredient, quantity);
        }
    }
}

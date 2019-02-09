package org.food.ministry.model.util;

import java.util.Map;

import org.food.ministry.model.Ingredient;

public abstract class Util {

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

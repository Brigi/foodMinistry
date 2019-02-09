package org.food.ministry.model;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.food.ministry.model.util.Util;

public abstract class AFoodStorage {

    private Map<Ingredient, Float> ingredients;
    
    public AFoodStorage() {
        this.ingredients = new HashMap<>();
    }
    
    public Map<Ingredient, Float> getIngredientsWithQuantity() {
        return this.ingredients;
    }
    
    public void addIngredient(Ingredient ingredient, float quantity) {
        Util.addIngredientToMap(this.ingredients, ingredient, quantity);
    }
    
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        TreeMap<Ingredient, Float> sortedMap = new TreeMap<>(getIngredientComparator());
        sortedMap.putAll(ingredients);
        sortedMap.forEach((ingredient,quantity) -> stringBuilder.append(MessageFormat.format("{0}: {1} {2}\r\n", ingredient.getName(), quantity, ingredient.getUnit())));
        
        return stringBuilder.toString();
    }
    
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

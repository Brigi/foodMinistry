package org.food.ministry.model;

import org.junit.Assert;
import org.junit.Test;

public class TestIngredient {

    @Test
    public void testGetNameAndUnit() {
        final String ingredientName = "Cucumber";
        final Unit ingredientUnit = Unit.NONE;
        Ingredient ingredient = new Ingredient(ingredientName, ingredientUnit, false);
        Assert.assertEquals(ingredientName, ingredient.getName());
        Assert.assertEquals(ingredientUnit, ingredient.getUnit());
    }
    
    @Test
    public void testSetNameAndUnit() {
        final String ingredientName = "Noodles";
        final Unit ingredientUnit = Unit.KILOGRAMM;
        Ingredient ingredient = new Ingredient("Cucumber", Unit.NONE, false);
        ingredient.setName(ingredientName);
        ingredient.setUnit(ingredientUnit);
        Assert.assertEquals(ingredientName, ingredient.getName());
        Assert.assertEquals(ingredientUnit, ingredient.getUnit());        
    }
    
    @Test
    public void testIsBasic() {
        Ingredient ingredient = new Ingredient("Cucumber", Unit.NONE, false);
        Assert.assertFalse(ingredient.isBasic());
        ingredient.setBasic(true);
        Assert.assertTrue(ingredient.isBasic());
    }
    
    @Test
    public void testToString() {
        final String exptectedString = "Name: Noodles, Unit: kg, isBasic: false";
        Ingredient ingredient = new Ingredient("Noodles", Unit.KILOGRAMM, false);
        Assert.assertEquals(exptectedString, ingredient.toString());
    }
    
    @Test
    public void testIdentity() {
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        Assert.assertTrue(zucchini.equals(zucchini));
    }
    
    @Test
    public void testEqualsTrue() {
        Ingredient zucchini1 = new Ingredient("Zucchini", Unit.NONE, false);
        Ingredient zucchini2 = new Ingredient("Zucchini", Unit.NONE, false);
        Assert.assertTrue(zucchini1.equals(zucchini2));
    }
    
    @Test
    public void testEqualsTrueWithNullName() {
        Ingredient noIngredient1 = new Ingredient(null, Unit.NONE, false);
        Ingredient noIngredient2 = new Ingredient(null, Unit.NONE, false);
        Assert.assertTrue(noIngredient1.equals(noIngredient2));
    }
    
    @Test
    public void testEqualsWrongName() {
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        Ingredient tomate = new Ingredient("Tomato", Unit.NONE, false);
        Assert.assertFalse(zucchini.equals(tomate));
    }
    
    @Test
    public void testEqualsWrongNameWithNull() {
        Ingredient noIngredient = new Ingredient(null, null, false);
        Ingredient tomate = new Ingredient("Tomato", Unit.NONE, false);
        Assert.assertFalse(noIngredient.equals(tomate));
    }
    
    @Test
    public void testEqualsWrongUnit() {
        Ingredient zucchini1 = new Ingredient("Zucchini", Unit.NONE, false);
        Ingredient zucchini2 = new Ingredient("Zucchini", Unit.KILOGRAMM, false);
        Assert.assertFalse(zucchini1.equals(zucchini2));
    }
    
    @Test
    public void testEqualsWrongIsBasic() {
        Ingredient salt1 = new Ingredient("Salt", Unit.TEASPOON, false);
        Ingredient salt2 = new Ingredient("Salt", Unit.TEASPOON, true);
        Assert.assertFalse(salt1.equals(salt2));
    }
    
    @Test
    public void testEqualsNull() {
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        Assert.assertFalse(zucchini.equals(null));
    }
    
    @Test
    public void testEqualsWrongObject() {
        Ingredient zucchini = new Ingredient("Zucchini", Unit.NONE, false);
        User user = new User("dummy", "dummy");
        Assert.assertFalse(zucchini.equals(user));
    }
}

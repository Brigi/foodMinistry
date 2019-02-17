package org.food.ministry.model;

import org.junit.Assert;
import org.junit.Test;

public class TestIngredient {

    @Test
    public void testGetNameAndUnit() {
        final String ingredientName = "Cucumber";
        final Unit ingredientUnit = Unit.NONE;
        Ingredient ingredient = new Ingredient(0, ingredientName, ingredientUnit, false);
        Assert.assertEquals(ingredientName, ingredient.getName());
        Assert.assertEquals(ingredientUnit, ingredient.getUnit());
    }

    @Test
    public void testSetNameAndUnit() {
        final String ingredientName = "Noodles";
        final Unit ingredientUnit = Unit.KILOGRAMM;
        Ingredient ingredient = new Ingredient(0, "Cucumber", Unit.NONE, false);
        ingredient.setName(ingredientName);
        ingredient.setUnit(ingredientUnit);
        Assert.assertEquals(ingredientName, ingredient.getName());
        Assert.assertEquals(ingredientUnit, ingredient.getUnit());
    }

    @Test
    public void testIsBasic() {
        Ingredient ingredient = new Ingredient(0, "Cucumber", Unit.NONE, false);
        Assert.assertFalse(ingredient.isBasic());
        ingredient.setBasic(true);
        Assert.assertTrue(ingredient.isBasic());
    }

    @Test
    public void testToString() {
        final String exptectedString = "Name: Noodles, Unit: kg, isBasic: false";
        Ingredient ingredient = new Ingredient(0, "Noodles", Unit.KILOGRAMM, false);
        Assert.assertEquals(exptectedString, ingredient.toString());
    }

    @Test
    public void testIdentity() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Assert.assertTrue(zucchini.equals(zucchini));
    }

    @Test
    public void testEqualsTrue() {
        Ingredient zucchini1 = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Ingredient zucchini2 = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Assert.assertTrue(zucchini1.equals(zucchini2));
    }

    @Test
    public void testEqualsTrueWithNullName() {
        Ingredient noIngredient1 = new Ingredient(0, null, Unit.NONE, false);
        Ingredient noIngredient2 = new Ingredient(0, null, Unit.NONE, false);
        Assert.assertTrue(noIngredient1.equals(noIngredient2));
    }

    @Test
    public void testEqualsWrongName() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Ingredient tomate = new Ingredient(0, "Tomato", Unit.NONE, false);
        Assert.assertFalse(zucchini.equals(tomate));
    }

    @Test
    public void testEqualsWrongNameWithNull() {
        Ingredient noIngredient = new Ingredient(0, null, null, false);
        Ingredient tomate = new Ingredient(0, "Tomato", Unit.NONE, false);
        Assert.assertFalse(noIngredient.equals(tomate));
    }

    @Test
    public void testEqualsWrongUnit() {
        Ingredient zucchini1 = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Ingredient zucchini2 = new Ingredient(0, "Zucchini", Unit.KILOGRAMM, false);
        Assert.assertFalse(zucchini1.equals(zucchini2));
    }

    @Test
    public void testEqualsWrongIsBasic() {
        Ingredient salt1 = new Ingredient(0, "Salt", Unit.TEASPOON, false);
        Ingredient salt2 = new Ingredient(0, "Salt", Unit.TEASPOON, true);
        Assert.assertFalse(salt1.equals(salt2));
    }

    @Test
    public void testEqualsWrongId() {
        Ingredient salt1 = new Ingredient(0, "Salt", Unit.TEASPOON, true);
        Ingredient salt2 = new Ingredient(1, "Salt", Unit.TEASPOON, true);
        Assert.assertFalse(salt1.equals(salt2));
    }

    @Test
    public void testEqualsNull() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        Assert.assertFalse(zucchini.equals(null));
    }

    @Test
    public void testEqualsWrongObject() {
        Ingredient zucchini = new Ingredient(0, "Zucchini", Unit.NONE, false);
        User user = new User(0, "dummy", "dummy", "dummy");
        Assert.assertFalse(zucchini.equals(user));
    }
}

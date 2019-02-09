package org.food.ministry.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestFoodInventory.class,
    TestHousehold.class,
    TestIngredient.class,
    TestIngredientsPool.class,
    TestRecipe.class,
    TestShoppingList.class,
    TestUser.class,
 })

public class AllTests {
    
}

package org.food.ministry.actors.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestFoodInventoryActor.class, TestHouseholdActor.class, TestIngredientActor.class, TestIngredientsPoolActor.class, TestRecipeActor.class, TestRecipesPoolActor.class, TestUserActor.class })

public class AllTests {

}
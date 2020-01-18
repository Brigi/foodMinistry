import { Injectable } from '@angular/core';
import { Recipe } from './recipe';
import { RECIPES } from './mock-recipes';
import { Ingredient } from '../ingredient/ingredient';

@Injectable({
  providedIn: 'root'
})
export class RecipesService {
  private recipes: Map<number, Recipe> = RECIPES;

  constructor() {}

  getRecipes(): Recipe[] {
    return Array.from(this.recipes.values());
  }

  getRestrictedRecipes(ingredients: Map<number, Ingredient>): Recipe[] {
    const restrictedRecipes: Recipe[] = [];
    for (const recipe of Array.from(this.recipes.values())) {
      let containsNoMoreIngredients = true;
      for (const recipeIngredient of Array.from(recipe.ingredients.values())) {
        if (!ingredients.has(recipeIngredient.id)) {
          containsNoMoreIngredients = false;
          break;
        }
      }
      if (containsNoMoreIngredients) {
        restrictedRecipes.push(recipe);
      }
    }

    return restrictedRecipes;
  }

  getInspiringRecipes(ingredients: Map<number, Ingredient>): Recipe[] {
    const inspiringRecipes: Recipe[] = [];
    for (const recipe of Array.from(this.recipes.values())) {
      let containsAllIngredients = true;
      for (const ingredient of Array.from(ingredients.values())) {
        if (!recipe.ingredients.has(ingredient.id)) {
          containsAllIngredients = false;
          break;
        }
      }
      if (containsAllIngredients) {
        inspiringRecipes.push(recipe);
      }
    }

    return inspiringRecipes;
  }
}

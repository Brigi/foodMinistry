import { Injectable } from '@angular/core';
import { Ingredient } from '../ingredient/ingredient';
import { INGREDIENTS } from './mock-ingredients';

@Injectable({
  providedIn: 'root'
})
export class IngredientsService {
  ingredients: Map<number, Ingredient> = INGREDIENTS;

  constructor() { }

  getIngredientsIds(): number[] {
    return Array.from(this.ingredients.keys());
  }

  getIngredients(): Map<number, Ingredient> {
    return this.ingredients;
  }

  getMissingIngredients(availableIngredients: Ingredient[]) {
    return this.getMissingIngredientsFromPool(availableIngredients, Array.from(this.ingredients.values()));
  }

  getMissingIngredientsFromPool(availableIngredients: Ingredient[], ingredientPool: Ingredient[]): Ingredient[] {
    const missingIngredients: Ingredient[] = [];
    for (const ingredient of ingredientPool) {
      let isAvailable = false;
      for (const availableIngredient of availableIngredients) {
        if (ingredient.id === availableIngredient.id) {
          isAvailable = true;
          break;
        }
      }
      if (!isAvailable) {
        missingIngredients.push(ingredient);
      }
    }

    return missingIngredients;
  }

  submitIngredient(ingredient: Ingredient) {
    this.ingredients.set(ingredient.id, ingredient);
  }
}

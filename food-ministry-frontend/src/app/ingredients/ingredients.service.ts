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

  submitIngredient(ingredient: Ingredient) {
    this.ingredients.set(ingredient.id, ingredient);
  }
}

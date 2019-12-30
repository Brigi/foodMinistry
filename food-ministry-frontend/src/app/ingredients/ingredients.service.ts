import { Injectable } from '@angular/core';
import { Ingredient } from './ingredient';
import { INGREDIENTS } from './mock-ingredients';

@Injectable({
  providedIn: 'root'
})
export class IngredientsService {

  ingredients: Map<number, Ingredient> = INGREDIENTS;

  constructor() { }

  getIngredients(): Map<number, Ingredient> {
    return this.ingredients;
  }

  submitIngredient(ingredient: Ingredient) {
    this.ingredients.set(ingredient.id, ingredient);
  }
}

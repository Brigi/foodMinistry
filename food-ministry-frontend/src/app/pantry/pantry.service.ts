import { Injectable } from '@angular/core';
import { INGREDIENTS } from './mock-pantry';
import { Ingredient } from '../ingredient/ingredient';

@Injectable({
  providedIn: 'root'
})
export class PantryService {
  private ingredients: Map<number, [Ingredient, number]> = INGREDIENTS;

  constructor() { }

  getIngredients(): Ingredient[] {
    const ingredientsOnly: Ingredient[] = [];
    for (const item of Array.from(this.ingredients.values())) {
      ingredientsOnly.push(item[0]);
    }
    return ingredientsOnly;
  }

  getIngredientsWithAmount(): [Ingredient, number][] {
    return Array.from(this.ingredients.values());
  }

  removeIngredient(id: number): void {
    console.log('Calling on ' + this.ingredients);
    this.ingredients.delete(id);
  }

  getAmount(ingredient: Ingredient): number {
    if (this.ingredients.has(ingredient.id)) {
      return this.ingredients.get(ingredient.id)[1];
    }
    return 0;
  }

  submitIngredient(ingredient: Ingredient, amount: number): void {
    this.ingredients.set(ingredient.id, [ingredient, amount]);
  }
}

import { Injectable } from '@angular/core';
import { Ingredient } from './ingredient';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  ingredient: Ingredient;

  constructor() { }

  getIngredient(): Ingredient {
    return this.ingredient;
  }

  getId(): number {
    return this.ingredient.id;
  }

  getName(): string {
    return this.ingredient.name;
  }

  getUnit(): string {
    return this.ingredient.unit;
  }

  isBasic(): boolean {
    return this.ingredient.isBasic;
  }

  setIngredient(ingredient: Ingredient): void {
    this.ingredient = ingredient;
  }
}

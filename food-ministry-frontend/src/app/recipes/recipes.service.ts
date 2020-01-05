import { Injectable } from '@angular/core';
import { Recipe } from './recipe';
import { RECIPES } from './mock-recipes';

@Injectable({
  providedIn: 'root'
})
export class RecipesService {
  private recipes: Map<number, Recipe> = RECIPES;

  constructor() {}

  getRecipes(): Recipe[] {
    return Array.from(this.recipes.values());
  }
}

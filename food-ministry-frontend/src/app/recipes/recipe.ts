import { Ingredient } from '../ingredient/ingredient';

export class Recipe {
  id: number;
  name: string;
  description: string;
  ingredients: Ingredient[];

  constructor(id: number, name: string, description: string, ingredients: Ingredient[]) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.ingredients = ingredients;
  }
}

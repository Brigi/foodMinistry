import { Ingredient } from '../ingredient/ingredient';

export const INGREDIENTS: Map<number, [Ingredient, number]> = new Map([
  [1, [new Ingredient(1, 'Banana', 'none', false), 3]],
  [2, [new Ingredient(2, 'Salt', 'tsp.', true), 100]],
  [3, [new Ingredient(3, 'Tomato', 'none', false), 5]],
  [4, [new Ingredient(4, 'Milk', 'l', false), 3]],
  [5, [new Ingredient(5, 'Flour', 'kg', true), 3]],
  [6, [new Ingredient(6, 'Salad', 'none', false), 1]],
  [7, [new Ingredient(7, 'Apple', 'none', false), 6]],
  [8, [new Ingredient(8, 'Sugar', 'tbsp.', true), 25]],
  [9, [new Ingredient(9, 'Olive Oil', 'ml', true), 500]]
]);

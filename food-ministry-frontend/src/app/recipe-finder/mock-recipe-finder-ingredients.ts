import { Ingredient } from '../ingredient/ingredient';

export const RECIPE_FINDER_INGREDIENTS: Map<number, Ingredient> = new Map([
  [2, { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }],
  [3, { id: 3, name: 'Tomato', unit: 'none', isBasic: false }],
  [9, { id: 9, name: 'Olive Oil', unit: 'ml', isBasic: true }],
  [12, { id: 12, name: 'Vinegar', unit: 'ml', isBasic: true }],
  [13, { id: 13, name: 'Onions', unit: 'none', isBasic: false }],
  [14, { id: 14, name: 'Pepper', unit: 'tsp.', isBasic: true }]
]);

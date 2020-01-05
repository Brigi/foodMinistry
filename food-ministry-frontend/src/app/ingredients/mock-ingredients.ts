import { Ingredient } from '../ingredient/ingredient';

export const INGREDIENTS: Map<number, Ingredient> = new Map([
  [1, { id: 1, name: 'Banana', unit: 'none', isBasic: false }],
  [2, { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }],
  [3, { id: 3, name: 'Tomato', unit: 'none', isBasic: false }],
  [4, { id: 4, name: 'Milk', unit: 'l', isBasic: false }],
  [5, { id: 5, name: 'Flour', unit: 'kg', isBasic: true }],
  [6, { id: 6, name: 'Salad', unit: 'none', isBasic: false }],
  [7, { id: 7, name: 'Apple', unit: 'none', isBasic: false }],
  [8, { id: 8, name: 'Sugar', unit: 'tbsp.', isBasic: true }],
  [9, { id: 9, name: 'Olive Oil', unit: 'ml', isBasic: true }],
  [10, { id: 10, name: 'Egg', unit: 'none', isBasic: false }],
  [11, { id: 11, name: 'Wine', unit: 'ml', isBasic: true }],
  [12, { id: 12, name: 'Vinegar', unit: 'ml', isBasic: true }],
  [13, { id: 13, name: 'Onions', unit: 'none', isBasic: false }],
  [14, { id: 14, name: 'Pepper', unit: 'tsp.', isBasic: true }]
]);

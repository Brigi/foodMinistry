import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  name: string;
  unit: string;
  isIngredientBasic: boolean;

  constructor() { }

  getName(): string {
    return this.name;
  }

  getUnit(): string {
    return this.unit;
  }

  isBasic(): boolean {
    return this.isIngredientBasic;
  }

  setName(name: string): void {
    this.name = name;
  }

  setUnit(unit: string): void {
    this.unit = unit;
  }

  setBasic(isIngredientBasic: boolean): void {
    this.isIngredientBasic = isIngredientBasic;
  }
}

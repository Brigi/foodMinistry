import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  id: number;
  name: string;
  unit: string;
  isIngredientBasic: boolean;

  constructor() { }

  getId(): number {
    return this.id;
  }

  getName(): string {
    return this.name;
  }

  getUnit(): string {
    return this.unit;
  }

  isBasic(): boolean {
    return this.isIngredientBasic;
  }

  setId(id: number): void {
    this.id = id;
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

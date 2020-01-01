export class Ingredient {
  id: number;
  name: string;
  unit: string;
  isBasic: boolean;

  constructor(id: number, name: string, unit: string, isBasic: boolean) {
    this.id = id;
    this.name = name;
    this.unit = unit;
    this.isBasic = isBasic;
  }
}

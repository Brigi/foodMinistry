import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';
import { Ingredient } from '../ingredient/ingredient';
import { PantryService } from './pantry.service';

@Component({
  selector: 'app-pantry',
  templateUrl: './pantry.component.html',
  styleUrls: ['../app.component.css', './pantry.component.css']
})
export class PantryComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientService: IngredientService;

  ingredients: [Ingredient, number][];

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService, pantryService: PantryService) {
    this.navigatorService = navigatorService;
    this.ingredientService = ingredientService;
    this.ingredients = pantryService.getIngredientsWithAmount();
  }

  ngOnInit() {
  }

  getLabel(ingredient: Ingredient, amount: number): string {
    if (ingredient.unit === 'none') {
      return amount + 'x ' + ingredient.name;
    }

    return amount + ' ' + ingredient.unit + ' ' + ingredient.name;
  }

  onBack(): void {
    this.navigatorService.setHomeStage();
  }

  onIngredientSelect(): void;
  onIngredientSelect(id?: number, name?: string, unit?: string, isBasic?: boolean): void {
    let ingredient: Ingredient;
    if (arguments.length === 0) {
      ingredient = new Ingredient(-1, 'New Ingredient', 'none', false);
    } else {
      ingredient = new Ingredient(id, name, unit, isBasic);
    }
    this.ingredientService.setIngredient(ingredient);
    console.log('Id: ' + this.ingredientService.getId());
    console.log('Name: ' + this.ingredientService.getName());
    console.log('Unit: ' + this.ingredientService.getUnit());
    console.log('IsBasic: ' + this.ingredientService.isBasic());
    this.navigatorService.setPantryDialogStage();
  }
}

import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';
import { IngredientsService } from './ingredients.service';
import { Ingredient } from './ingredient';
import { INGREDIENTS } from './mock-ingredients';

@Component({
  selector: 'app-ingredients',
  templateUrl: './ingredients.component.html',
  styleUrls: ['../app.component.css', './ingredients.component.css']
})
export class IngredientsComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientService: IngredientService;
  ingredientsService: IngredientsService;

  ingredients: Map<number, Ingredient>;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService, ingredientsService: IngredientsService) {
    this.navigatorService = navigatorService;
    this.ingredientService = ingredientService;
    this.ingredientsService = ingredientsService;
    this.ingredients = ingredientsService.getIngredients();
  }

  ngOnInit() {
  }

  onIngredientSelect(): void;
  onIngredientSelect(id?: number, name?: string, unit?: string, isBasic?: boolean): void {
    if (arguments.length === 0) {
      this.ingredientService.setId(-1);
      this.ingredientService.setName('New Ingredient');
      this.ingredientService.setUnit('none');
      this.ingredientService.setBasic(false);
    } else {
      this.ingredientService.setId(id);
      this.ingredientService.setName(name);
      this.ingredientService.setUnit(unit);
      this.ingredientService.setBasic(isBasic);
    }
    console.log('Id: ' + this.ingredientService.getId());
    console.log('Name: ' + this.ingredientService.getName());
    console.log('Unit: ' + this.ingredientService.getUnit());
    console.log('IsBasic: ' + this.ingredientService.isBasic());
    this.navigatorService.setIngredientDialogStage();
  }
}

import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';
import { IngredientsService } from '../ingredients/ingredients.service';
import { Ingredient } from '../ingredient/ingredient';

@Component({
  selector: 'app-ingredient-dialog',
  templateUrl: './ingredient-dialog.component.html',
  styleUrls: ['../app.component.css', './ingredient-dialog.component.css']
})
export class IngredientDialogComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientsService: IngredientsService;
  id: number;
  name: string;
  unit: string;
  isBasic: boolean;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService, ingredientsService: IngredientsService) {
    this.navigatorService = navigatorService;
    this.ingredientsService = ingredientsService;
    this.id = ingredientService.getId();
    this.name = ingredientService.getName();
    this.unit = ingredientService.getUnit();
    this.isBasic = ingredientService.isBasic();
  }

  ngOnInit() {
  }

  onSave() {
    console.log('Submitting ingredient:');
    console.log('Id: ' + this.id);
    console.log('Name: ' + this.name);
    console.log('Unit: ' + this.unit);
    console.log('IsBasic: ' + this.isBasic);
    this.ingredientsService.submitIngredient({id: this.id, name: this.name, unit: this.unit, isBasic: this.isBasic});
    this.navigatorService.setIngredientsStage();
  }

  onCancel() {
    this.navigatorService.setIngredientsStage();
  }
}

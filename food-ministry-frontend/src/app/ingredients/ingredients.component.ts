import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';
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

  ingredients: Ingredient[] = INGREDIENTS;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService) {
    this.navigatorService = navigatorService;
    this.ingredientService = ingredientService;
  }

  ngOnInit() {
  }

  onIngredientSelect(): void;
  onIngredientSelect(name?: string, unit?: string, isBasic?: boolean): void {
    if (arguments.length === 0) {
      this.ingredientService.setName('Name');
      this.ingredientService.setUnit('none');
      this.ingredientService.setBasic(false);
    } else {
      this.ingredientService.setName(name);
      this.ingredientService.setUnit(unit);
      this.ingredientService.setBasic(isBasic);
    }
    this.navigatorService.setIngredientDialogStage();
  }
}

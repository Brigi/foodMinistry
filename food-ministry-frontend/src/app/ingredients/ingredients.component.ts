import { Component, OnInit, AfterViewInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';
import { IngredientsService } from './ingredients.service';
import { Ingredient } from '../ingredient/ingredient';

@Component({
  selector: 'app-ingredients',
  templateUrl: './ingredients.component.html',
  styleUrls: ['../app.component.css', './ingredients.component.css']
})
export class IngredientsComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientService: IngredientService;

  ingredients: Map<number, Ingredient>;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService, ingredientsService: IngredientsService) {
    this.navigatorService = navigatorService;
    this.ingredientService = ingredientService;
    this.ingredients = ingredientsService.getIngredients();
  }

  ngOnInit() {
  }

  getIngredientsAsArray(): Ingredient[] {
    return Array.from(this.ingredients.values());
  }

  onBack(): void {
    this.navigatorService.setHomeStage();
  }

  onIngredientSelect(): void;
  onIngredientSelect(ingredientId?: number, ingredientName?: string, ingredientUnit?: string, ingredientIsBasic?: boolean): void {
    if (arguments.length === 0) {
      this.ingredientService.setIngredient({id: -1, name: 'New Ingredient', unit: 'none', isBasic: false});
    } else {
      this.ingredientService.setIngredient({id: ingredientId, name: ingredientName, unit: ingredientUnit, isBasic: ingredientIsBasic});
    }
    console.log('Id: ' + this.ingredientService.getId());
    console.log('Name: ' + this.ingredientService.getName());
    console.log('Unit: ' + this.ingredientService.getUnit());
    console.log('IsBasic: ' + this.ingredientService.isBasic());
    this.navigatorService.setIngredientDialogStage();
  }
}

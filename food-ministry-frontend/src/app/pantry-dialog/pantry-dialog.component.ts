import { Component, OnInit } from '@angular/core';
import { MatOptionSelectionChange } from '@angular/material';
import { NavigatorService } from '../navigator/navigator.service';
import { PantryService } from '../pantry/pantry.service';
import { IngredientService } from '../ingredient/ingredient.service';
import { IngredientsService } from '../ingredients/ingredients.service';
import { Ingredient } from '../ingredient/ingredient';

@Component({
  selector: 'app-pantry-dialog',
  templateUrl: './pantry-dialog.component.html',
  styleUrls: ['../app.component.css', './pantry-dialog.component.css']
})
export class PantryDialogComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientsService: IngredientsService;
  pantryService: PantryService;
  id: number;
  name: string;
  unit: string;
  isBasic: boolean;
  newIngredient: boolean;
  amount: number;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService,
              ingredientsService: IngredientsService, pantryService: PantryService) {
    this.navigatorService = navigatorService;
    this.ingredientsService = ingredientsService;
    this.pantryService = pantryService;
    this.id = ingredientService.getId();
    this.name = ingredientService.getName();
    this.unit = ingredientService.getUnit();
    this.isBasic = ingredientService.isBasic();
    this.newIngredient = this.id === -1;
    this.amount = pantryService.getAmount(ingredientService.getIngredient());
    console.log('Amount: ' + this.amount);
  }

  ngOnInit() {
  }

  getEmptyIngredients(): Ingredient[] {
    const emptyIngredients: Ingredient[] = [];
    for (const availableIngredient of Array.from(this.ingredientsService.getIngredients())) {
      let isInPantry = false;
      for (const pantryIngredient of this.pantryService.getIngredientsWithAmount()) {
        if (availableIngredient[0] === pantryIngredient[0].id) {
          isInPantry = true;
          break;
        }
      }
      if (!isInPantry) {
        emptyIngredients.push(availableIngredient[1]);
      }
    }
    return emptyIngredients;
  }

  getUnit(): string {
    return this.unit === 'none' ? '' : this.unit;
  }

  onIngredientSelect(event: MatOptionSelectionChange, selectedId: number): void {
    const selectedIngredient: Ingredient = this.ingredientsService.getIngredients().get(selectedId);
    this.id = selectedIngredient.id;
    this.name = selectedIngredient.name;
    this.unit = selectedIngredient.unit;
    this.isBasic = selectedIngredient.isBasic;
    console.log(this.name + ' selected');
  }

  onSave(): void {
    console.log('Submitting ingredient:');
    console.log('Id: ' + this.id);
    console.log('Name: ' + this.name);
    console.log('Amount: ' + this.amount);
    console.log('Unit: ' + this.unit);
    console.log('IsBasic: ' + this.isBasic);
    this.pantryService.submitIngredient({id: this.id, name: this.name, unit: this.unit, isBasic: this.isBasic}, this.amount);
    this.navigatorService.setPantryStage();
  }

  onCancel(): void {
    this.navigatorService.setPantryStage();
  }
}

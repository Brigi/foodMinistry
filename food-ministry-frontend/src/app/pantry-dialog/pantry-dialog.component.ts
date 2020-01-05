import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
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
export class PantryDialogComponent implements OnInit, AfterViewInit {
  static AMOUNT_REGEX: RegExp = new RegExp('/^-?\d*[.,]?\d*$/');
  @ViewChild('amount', {static: true}) amountBox: ElementRef;

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

  ngAfterViewInit() {

  }

  checkAmountInput(evt: KeyboardEvent): void {
    console.log('checking input');
    PantryDialogComponent.AMOUNT_REGEX.test('' + this.amount);
  }

  getMissingIngredients(): Ingredient[] {
    return this.ingredientsService.getMissingIngredients(this.pantryService.getIngredients());
  }

  getUnit(): string {
    return this.unit === 'none' ? '' : this.unit;
  }

  isInvalidInput(): boolean {
    console.log('isInvalidInput called with following parameters: id: ' + this.id + ', this.amount: ' + this.amount);
    return this.id <= 0 || this.amount <= 0 || this.amount === undefined;
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

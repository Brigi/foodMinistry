import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { IngredientService } from '../ingredient/ingredient.service';

@Component({
  selector: 'app-ingredient-dialog',
  templateUrl: './ingredient-dialog.component.html',
  styleUrls: ['../app.component.css', './ingredient-dialog.component.css']
})
export class IngredientDialogComponent implements OnInit {
  navigatorService: NavigatorService;
  ingredientService: IngredientService;
  name: string;
  unit: string;
  isBasic: boolean;

  constructor(navigatorService: NavigatorService, ingredientService: IngredientService) {
    this.navigatorService = navigatorService;
    this.ingredientService = ingredientService;
    this.name = ingredientService.getName();
    this.unit = ingredientService.getUnit();
    this.isBasic = ingredientService.isBasic();
  }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { Ingredient } from './ingredient';
import { INGREDIENTS } from './mock-ingredients';

@Component({
  selector: 'app-ingredients',
  templateUrl: './ingredients.component.html',
  styleUrls: ['../app.component.css', './ingredients.component.css']
})
export class IngredientsComponent implements OnInit {
  navigatorService: NavigatorService;

  ingredients = INGREDIENTS;

  constructor(navigatorService: NavigatorService) {
    this.navigatorService = navigatorService;
  }

  ngOnInit() {
  }

  onIngredientSelect(): void {
    this.navigatorService.setIngredientDialogStage();
  }
}

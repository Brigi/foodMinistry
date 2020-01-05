import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NavigatorService } from '../navigator/navigator.service';
import { RecipesService } from './recipes.service';
import { Recipe } from './recipe';
import { Ingredient } from '../ingredient/ingredient';
import { AddIngredientsDialogComponent, DialogData } from '../add-ingredients-dialog/add-ingredients-dialog.component';

import { RECIPE_FINDER_INGREDIENTS } from './mock-recipe-finder-ingredients';
import { IngredientsService } from '../ingredients/ingredients.service';

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['../app.component.css', './recipes.component.css']
})
export class RecipesComponent implements OnInit {
  dialog: MatDialog;
  navigatorService: NavigatorService;
  recipesService: RecipesService;
  ingredientsService: IngredientsService;
  recipes: Recipe[];
  recipeFinderIngredients: Ingredient[] = RECIPE_FINDER_INGREDIENTS;

  constructor(navigatorService: NavigatorService, recipesService: RecipesService,
              ingredientsService: IngredientsService, dialog: MatDialog) {
    this.navigatorService = navigatorService;
    this.recipesService = recipesService;
    this.ingredientsService = ingredientsService;
    this.dialog = dialog;
    this.recipes = recipesService.getRecipes();
  }

  ngOnInit() {
  }

  onBack(): void {
    this.navigatorService.setHomeStage();
  }

  onRecipeSelect(): void {
    console.log('Recipe selected');
  }

  onAddIngredient(): void {
    console.log('Ingredient selected');
    const dialogData: DialogData = {ingredients: this.ingredientsService.getMissingIngredients(this.recipeFinderIngredients)};
    const dialogRef = this.dialog.open(AddIngredientsDialogComponent, {
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        for (const ingredient of result as Ingredient[]) {
          console.log('Adding ingredient: ' + ingredient.name);
          this.recipeFinderIngredients.push(ingredient);
        }
        //this.recipeFinderIngredients.push(result);
      }
    });
  }
}

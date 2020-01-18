import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Ingredient } from '../ingredient/ingredient';
import { AddIngredientsDialogComponent, DialogData } from '../add-ingredients-dialog/add-ingredients-dialog.component';
import { IngredientsService } from '../ingredients/ingredients.service';

import { RECIPE_FINDER_INGREDIENTS } from './mock-recipe-finder-ingredients';
import { RecipesService } from '../recipes/recipes.service';
import { Recipe } from '../recipes/recipe';

@Component({
  selector: 'app-recipe-finder',
  templateUrl: './recipe-finder.component.html',
  styleUrls: ['./recipe-finder.component.css']
})
export class RecipeFinderComponent implements OnInit {
  dialog: MatDialog;
  ingredientsService: IngredientsService;
  recipesService: RecipesService;
  recipeFinderIngredients: Map<number, Ingredient> = RECIPE_FINDER_INGREDIENTS;

  @Output() foundRecipes = new EventEmitter<Recipe[]>();

  constructor(ingredientsService: IngredientsService,
              recipesService: RecipesService,
              dialog: MatDialog) {
    this.ingredientsService = ingredientsService;
    this.recipesService = recipesService;
    this.dialog = dialog;
  }

  ngOnInit() {
  }

  getRecipeFinderIngredients(): Ingredient[] {
    return Array.from(this.recipeFinderIngredients.values());
  }

  onRemove(id: number): void {
    this.recipeFinderIngredients.delete(id);
  }

  onAddIngredient(): void {
    console.log('Ingredient selected');
    const dialogData: DialogData = {ingredients: this.ingredientsService.getMissingIngredients(this.getRecipeFinderIngredients())};
    const dialogRef = this.dialog.open(AddIngredientsDialogComponent, {
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        for (const ingredient of result as Ingredient[]) {
          console.log('Adding ingredient: ' + ingredient.name);
          this.recipeFinderIngredients.set(ingredient.id, ingredient);
        }
      }
    });
  }

  onRestrictiveSearch(): void {
    this.foundRecipes.emit(this.recipesService.getRestrictedRecipes(this.recipeFinderIngredients));
  }

  onOpenSearch(): void {
    this.foundRecipes.emit(this.recipesService.getInspiringRecipes(this.recipeFinderIngredients));
  }
}

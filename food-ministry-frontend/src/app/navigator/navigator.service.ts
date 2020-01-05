import { Injectable } from '@angular/core';

enum Stage {
  Login,
  Home,
  Ingredients,
  IngredientDialog,
  Pantry,
  PantryDialog,
  Recipes
}

@Injectable({
  providedIn: 'root'
})
export class NavigatorService {
  stage = Stage.Login;

  constructor() { }

  get isLoginStage(): boolean {
    return this.stage === Stage.Login;
  }

  get isHomeStage(): boolean {
    return this.stage === Stage.Home;
  }

  get isIngredientsStage(): boolean {
    return this.stage === Stage.Ingredients;
  }

  get isIngredientDialogStage(): boolean {
    return this.stage === Stage.IngredientDialog;
  }

  get isPantryStage(): boolean {
    return this.stage === Stage.Pantry;
  }

  get isPantryDialogStage(): boolean {
    return this.stage === Stage.PantryDialog;
  }

  get isRecipesStage(): boolean {
    return this.stage === Stage.Recipes;
  }

  setLoginStage(): void {
    this.stage = Stage.Login;
  }

  setHomeStage(): void {
    this.stage = Stage.Home;
  }

  setIngredientsStage(): void {
    this.stage = Stage.Ingredients;
  }

  setIngredientDialogStage(): void {
    this.stage = Stage.IngredientDialog;
  }

  setPantryStage(): void {
    this.stage = Stage.Pantry;
  }

  setPantryDialogStage(): void {
    this.stage = Stage.PantryDialog;
  }

  setRecipesStage(): void {
    this.stage = Stage.Recipes;
  }
}

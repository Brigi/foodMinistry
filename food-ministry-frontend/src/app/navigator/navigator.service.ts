import { Injectable } from '@angular/core';

enum Stage {
  Login,
  Home,
  Ingredients,
  IngredientDialog,
  Pantry,
  PantryDialog
}

@Injectable({
  providedIn: 'root'
})
export class NavigatorService {
  stage = Stage.Login;

  constructor() { }

  get isLoginStage() {
    return this.stage === Stage.Login;
  }

  get isHomeStage() {
    return this.stage === Stage.Home;
  }

  get isIngredientsStage() {
    return this.stage === Stage.Ingredients;
  }

  get isIngredientDialogStage() {
    return this.stage === Stage.IngredientDialog;
  }

  get isPantryStage() {
    return this.stage === Stage.Pantry;
  }

  get isPantryDialogStage() {
    return this.stage === Stage.PantryDialog;
  }

  setLoginStage() {
    this.stage = Stage.Login;
  }

  setHomeStage() {
    this.stage = Stage.Home;
  }

  setIngredientsStage() {
    this.stage = Stage.Ingredients;
  }

  setIngredientDialogStage() {
    this.stage = Stage.IngredientDialog;
  }

  setPantryStage() {
    this.stage = Stage.Pantry;
  }

  setPantryDialogStage() {
    this.stage = Stage.PantryDialog;
  }
}

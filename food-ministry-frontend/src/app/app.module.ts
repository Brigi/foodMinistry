import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { IngredientsComponent } from './ingredients/ingredients.component';
import { IngredientDialogComponent } from './ingredient-dialog/ingredient-dialog.component';
import { PantryComponent } from './pantry/pantry.component';
import { PantryDialogComponent } from './pantry-dialog/pantry-dialog.component';
import { RecipesComponent } from './recipes/recipes.component';
import { AddIngredientsDialogComponent } from './add-ingredients-dialog/add-ingredients-dialog.component';
import { ListRecipesComponent } from './list-recipes/list-recipes.component';
import { RecipeFinderComponent } from './recipe-finder/recipe-finder.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    IngredientsComponent,
    IngredientDialogComponent,
    PantryComponent,
    PantryDialogComponent,
    RecipesComponent,
    AddIngredientsDialogComponent,
    ListRecipesComponent,
    RecipeFinderComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MatSelectModule,
    MatTabsModule,
    MatDialogModule
  ],
  entryComponents: [
    AddIngredientsDialogComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

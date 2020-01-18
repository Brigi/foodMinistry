import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Recipe } from '../recipes/recipe';

@Component({
  selector: 'app-list-recipes',
  templateUrl: './list-recipes.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['../app.component.css', './list-recipes.component.css']
})
export class ListRecipesComponent implements OnInit {
  @Input() recipes: Recipe[];
  @Input() readOnly: boolean;

  constructor() { }

  ngOnInit() {
  }

  getRecipes(): Recipe[] {
    return this.recipes;
  }

  onRecipeSelect(): void {
    console.log('Recipe selected');
  }

  onAddRecipe(): void {
    if (!this.readOnly) {
      console.log('Adding Recipe');
    }
  }
}

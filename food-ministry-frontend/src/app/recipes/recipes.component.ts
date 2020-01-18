import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';
import { RecipesService } from './recipes.service';
import { Recipe } from './recipe';

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['../app.component.css', './recipes.component.css']
})
export class RecipesComponent implements OnInit {
  navigatorService: NavigatorService;
  recipesService: RecipesService;
  foundRecipes: Recipe[] = [];
  selectedTab = 0;

  constructor(navigatorService: NavigatorService, recipesService: RecipesService) {
    this.navigatorService = navigatorService;
    this.recipesService = recipesService;
  }

  ngOnInit() {
  }

  getRecipes(): Recipe[] {
    return this.recipesService.getRecipes();
  }

  onBack(): void {
    this.navigatorService.setHomeStage();
  }

  onSearch(foundRecipes: Recipe[]) {
    this.foundRecipes = foundRecipes;
    this.selectedTab = 2;
  }
}

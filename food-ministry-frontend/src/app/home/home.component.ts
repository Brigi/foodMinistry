import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['../app.component.css', './home.component.css']
})
export class HomeComponent implements OnInit {
  navigatorService: NavigatorService;

  constructor(navigatorService: NavigatorService) {
    this.navigatorService = navigatorService;
  }

  ngOnInit() {
  }

  onPantrySelect(): void {
    this.navigatorService.setPantryStage();
  }

  onIngredientsSelect(): void {
    this.navigatorService.setIngredientsStage();
  }
}

import { Component, OnInit } from '@angular/core';
import { NavigatorService } from './navigator/navigator.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'food Ministry';
  navigatorService: NavigatorService;

  constructor(navigatorService: NavigatorService) {
    this.navigatorService = navigatorService;
  }

  ngOnInit(): void { }
}

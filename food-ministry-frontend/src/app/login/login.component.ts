import { Component, OnInit } from '@angular/core';
import { NavigatorService } from '../navigator/navigator.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../app.component.css', './login.component.css']
})
export class LoginComponent implements OnInit {
  navigatorService: NavigatorService;

  constructor(navigatorService: NavigatorService) {
    this.navigatorService = navigatorService;
   }

  ngOnInit() {
  }

  onSelect(): void {
    this.navigatorService.setHomeStage();
  }
}

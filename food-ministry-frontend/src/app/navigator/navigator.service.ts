import { Injectable } from '@angular/core';

enum Stage {
  Login,
  Home
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

  setLoginStage() {
    this.stage = Stage.Login;
  }

  setHomeStage() {
    this.stage = Stage.Home;
  }
}

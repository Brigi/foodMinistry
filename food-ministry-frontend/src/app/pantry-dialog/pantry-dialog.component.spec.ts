import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PantryDialogComponent } from './pantry-dialog.component';

describe('PantryDialogComponent', () => {
  let component: PantryDialogComponent;
  let fixture: ComponentFixture<PantryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PantryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PantryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIngredientsDialogComponent } from './add-ingredients-dialog.component';

describe('AddIngredientsDialogComponent', () => {
  let component: AddIngredientsDialogComponent;
  let fixture: ComponentFixture<AddIngredientsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddIngredientsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddIngredientsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

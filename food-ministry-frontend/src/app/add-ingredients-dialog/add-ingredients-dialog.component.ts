import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Ingredient } from '../ingredient/ingredient';

export interface DialogData {
  ingredients: Ingredient[];
}

@Component({
  selector: 'app-add-ingredients-dialog',
  templateUrl: './add-ingredients-dialog.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['../app.component.css', './add-ingredients-dialog.component.css']
})
export class AddIngredientsDialogComponent implements OnInit {
  dialogRef: MatDialogRef<AddIngredientsDialogComponent>;
  data: DialogData;
  selectedIngredients: Map<number, Ingredient> = new Map<number, Ingredient>();

  constructor(dialogRef: MatDialogRef<AddIngredientsDialogComponent>, @Inject(MAT_DIALOG_DATA) data: DialogData) {
    this.dialogRef = dialogRef;
    this.data = data;
  }

  ngOnInit() {
  }

  isSelected(id: number): boolean {
    return this.selectedIngredients.has(id);
  }

  onIngredientSelect(ingredient: Ingredient): void {
    if (this.selectedIngredients.has(ingredient.id)) {
      this.selectedIngredients.delete(ingredient.id);
    } else {
      this.selectedIngredients.set(ingredient.id, ingredient);
    }
  }

  onCancel(): void {
    this.dialogRef.close({});
  }

  onSelect(): void {
    this.dialogRef.close(Array.from(this.selectedIngredients.values()));
  }
}

import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Ingredient } from '../ingredient/ingredient';
import { PantryService } from '../pantry/pantry.service';

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
  pantryService: PantryService;
  data: DialogData;
  selectedIngredients: Map<number, Ingredient> = new Map<number, Ingredient>();

  constructor(dialogRef: MatDialogRef<AddIngredientsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data: DialogData,
              pantryService: PantryService) {
    this.dialogRef = dialogRef;
    this.data = data;
    this.pantryService = pantryService;
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

  onAddPantry(): void {
    for (const pantryIngredient of this.pantryService.getIngredients()) {
      for (const dialogIngredient of this.data.ingredients) {
        if (dialogIngredient.id === pantryIngredient.id) {
          this.selectedIngredients.set(pantryIngredient.id, pantryIngredient);
          break;
        }
      }
    }
  }
}

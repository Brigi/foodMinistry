import { TestBed } from '@angular/core/testing';

import { PantryService } from './pantry.service';

describe('PantryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PantryService = TestBed.get(PantryService);
    expect(service).toBeTruthy();
  });
});

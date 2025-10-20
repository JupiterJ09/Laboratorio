import { TestBed } from '@angular/core/testing';

import { Insumo } from './insumo';

describe('Insumo', () => {
  let service: Insumo;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Insumo);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

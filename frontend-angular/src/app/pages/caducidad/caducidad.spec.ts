import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Caducidad } from './caducidad';

describe('Caducidad', () => {
  let component: Caducidad;
  let fixture: ComponentFixture<Caducidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Caducidad]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Caducidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoPrediccion } from './grafico-prediccion';

describe('GraficoPrediccion', () => {
  let component: GraficoPrediccion;
  let fixture: ComponentFixture<GraficoPrediccion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GraficoPrediccion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GraficoPrediccion);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

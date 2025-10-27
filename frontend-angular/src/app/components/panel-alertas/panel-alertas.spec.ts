import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanelAlertas } from './panel-alertas';

describe('PanelAlertas', () => {
  let component: PanelAlertas;
  let fixture: ComponentFixture<PanelAlertas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PanelAlertas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PanelAlertas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

/**
 * @file lote.ts (VERSIÓN MOCK ADAPTADA)
 * @description Servicio de lotes usando datos mock inline
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay, map } from 'rxjs';
import { Lote, LoteCaducidadDTO } from '../models/lote.interface';

// Función helper para generar fechas futuras
function getFechaFutura(dias: number): string {
  const fecha = new Date();
  fecha.setDate(fecha.getDate() + dias);
  return fecha.toISOString().split('T')[0];
}

// Datos mock inline
const LOTES_MOCK: LoteCaducidadDTO[] = [
  {
    id: 1,
    numeroLote: 'LT-2024-001',
    insumoNombre: 'Paracetamol 500mg',
    fechaCaducidad: getFechaFutura(180),
    cantidadActual: 45,
    diasParaCaducar: 180,
    estaVencido: false,
    proveedor: 'Farmacéutica del Sur',
    nivelAlerta: 'MEDIA'
  },
  {
    id: 2,
    numeroLote: 'LT-2024-089',
    insumoNombre: 'Gasas Estériles 10x10cm',
    fechaCaducidad: getFechaFutura(365),
    cantidadActual: 350,
    diasParaCaducar: 365,
    estaVencido: false,
    proveedor: 'Suministros Médicos SA',
    nivelAlerta: 'BAJA'
  },
  {
    id: 3,
    numeroLote: 'LT-2024-156',
    insumoNombre: 'Suero Fisiológico 1000ml',
    fechaCaducidad: getFechaFutura(240),
    cantidadActual: 180,
    diasParaCaducar: 240,
    estaVencido: false,
    proveedor: 'Laboratorios Unidos',
    nivelAlerta: 'MEDIA'
  },
  {
    id: 4,
    numeroLote: 'LT-2024-234',
    insumoNombre: 'Jeringas Desechables 5ml',
    fechaCaducidad: getFechaFutura(540),
    cantidadActual: 520,
    diasParaCaducar: 540,
    estaVencido: false,
    proveedor: 'Distribuidora Médica',
    nivelAlerta: 'BAJA'
  },
  {
    id: 5,
    numeroLote: 'LT-2024-167',
    insumoNombre: 'Antibiótico Amoxicilina 500mg',
    fechaCaducidad: getFechaFutura(90),
    cantidadActual: 85,
    diasParaCaducar: 90,
    estaVencido: false,
    proveedor: 'Farmacéutica del Sur',
    nivelAlerta: 'ALTA'
  },
  {
    id: 6,
    numeroLote: 'LT-2024-289',
    insumoNombre: 'Guantes de Látex Talla M',
    fechaCaducidad: getFechaFutura(450),
    cantidadActual: 120,
    diasParaCaducar: 450,
    estaVencido: false,
    proveedor: 'Equipos de Protección SA',
    nivelAlerta: 'BAJA'
  },
  {
    id: 7,
    numeroLote: 'LT-2024-312',
    insumoNombre: 'Alcohol Gel 70% 500ml',
    fechaCaducidad: getFechaFutura(300),
    cantidadActual: 65,
    diasParaCaducar: 300,
    estaVencido: false,
    proveedor: 'Productos de Higiene',
    nivelAlerta: 'MEDIA'
  },
  {
    id: 8,
    numeroLote: 'LT-2023-455',
    insumoNombre: 'Vendas Elásticas 10cm',
    fechaCaducidad: getFechaFutura(15),
    cantidadActual: 12,
    diasParaCaducar: 15,
    estaVencido: false,
    proveedor: 'Material Médico SA',
    nivelAlerta: 'CRITICA'
  }
];

@Injectable({
  providedIn: 'root'
})
export class LoteService {
  constructor() {
    console.log('✅ LoteService inicializado con datos mock inline');
  }

  // ==========================================
  // CRUD BÁSICO
  // ==========================================

  /**
   * Obtener todos los lotes
   */
  public getAllLotes(): Observable<Lote[]> {
    console.log('📦 Devolviendo todos los lotes mock');
    return of([...LOTES_MOCK] as any).pipe(delay(300));
  }

  /**
   * Obtener lotes activos
   */
  public getLotesActivos(): Observable<Lote[]> {
    const activos = LOTES_MOCK.filter(l => !l.estaVencido);
    console.log(`✅ Devolviendo ${activos.length} lotes activos`);
    return of(activos as any).pipe(delay(300));
  }

  /**
   * Obtener un lote por ID
   */
  public getLoteById(id: number): Observable<Lote | undefined> {
    const lote = LOTES_MOCK.find(l => l.id === id);
    console.log(`🔍 Buscando lote ID ${id}:`, lote ? 'Encontrado' : 'No encontrado');
    return of(lote as any).pipe(delay(200));
  }

  // ==========================================
  // ALERTAS DE CADUCIDAD ⭐ IMPORTANTE
  // ==========================================

  /**
   * ✅ Obtener lotes próximos a caducar
   * @param dias - Número de días hacia adelante (por defecto 7)
   */
  public getLotesProximosACaducar(dias: number = 7): Observable<LoteCaducidadDTO[]> {
    console.log(`📅 Filtrando lotes próximos a caducar en ${dias} días...`);
    
    const lotesFiltrados = LOTES_MOCK.filter(l => 
      l.diasParaCaducar <= dias && !l.estaVencido
    ).map(lote => {
      // Calcular nivel de alerta basado en días
      if (lote.estaVencido) {
        lote.nivelAlerta = 'CRITICA';
      } else if (lote.diasParaCaducar <= 7) {
        lote.nivelAlerta = 'CRITICA';
      } else if (lote.diasParaCaducar <= 30) {
        lote.nivelAlerta = 'ALTA';
      } else if (lote.diasParaCaducar <= 90) {
        lote.nivelAlerta = 'MEDIA';
      } else {
        lote.nivelAlerta = 'BAJA';
      }
      return lote;
    });

    console.log(`✅ ${lotesFiltrados.length} lotes encontrados próximos a caducar`);
    return of(lotesFiltrados).pipe(delay(300));
  }

  /**
   * Obtener lotes vencidos
   */
  public getLotesVencidos(): Observable<LoteCaducidadDTO[]> {
    const vencidos = LOTES_MOCK.filter(l => l.estaVencido);
    console.log(`📛 Devolviendo ${vencidos.length} lotes vencidos`);
    return of(vencidos).pipe(delay(300));
  }

  /**
   * Obtener lotes de un insumo próximos a caducar
   */
  public getLotesPorInsumoProximosCaducar(
    insumoId: number,
    dias: number = 30
  ): Observable<LoteCaducidadDTO[]> {
    // Simulación: filtrar por nombre del insumo que contenga el ID
    const lotesFiltrados = LOTES_MOCK.filter(l => 
      l.diasParaCaducar <= dias && !l.estaVencido
    );
    console.log(`🔍 Lotes de insumo #${insumoId} próximos a caducar: ${lotesFiltrados.length}`);
    return of(lotesFiltrados).pipe(delay(300));
  }

  // ==========================================
  // GESTIÓN DE STOCK
  // ==========================================

  /**
   * Obtener lotes con stock disponible
   */
  public getLotesConStock(): Observable<Lote[]> {
    const conStock = LOTES_MOCK.filter(l => l.cantidadActual > 0);
    console.log(`📦 Devolviendo ${conStock.length} lotes con stock`);
    return of(conStock as any).pipe(delay(300));
  }

  /**
   * Obtener lotes agotados
   */
  public getLotesAgotados(): Observable<Lote[]> {
    const agotados = LOTES_MOCK.filter(l => l.cantidadActual === 0);
    console.log(`❌ Devolviendo ${agotados.length} lotes agotados`);
    return of(agotados as any).pipe(delay(300));
  }

  /**
   * Obtener lotes ordenados por caducidad (FEFO)
   */
  public getLotesOrdenadosPorCaducidad(insumoId: number): Observable<Lote[]> {
    const ordenados = [...LOTES_MOCK].sort((a, b) => 
      a.diasParaCaducar - b.diasParaCaducar
    );
    console.log(`📊 Devolviendo lotes ordenados por caducidad`);
    return of(ordenados as any).pipe(delay(300));
  }

  // ==========================================
  // BÚSQUEDAS
  // ==========================================

  /**
   * Buscar lote por número
   */
  public buscarPorNumero(numeroLote: string): Observable<Lote | undefined> {
    const lote = LOTES_MOCK.find(l => l.numeroLote === numeroLote);
    console.log(`🔍 Buscando lote ${numeroLote}:`, lote ? 'Encontrado' : 'No encontrado');
    return of(lote as any).pipe(delay(200));
  }

  /**
   * Obtener lotes de un insumo
   */
  public getLotesPorInsumo(insumoId: number): Observable<Lote[]> {
    // Simulación: devolver algunos lotes aleatorios
    const lotes = LOTES_MOCK.slice(0, 3);
    console.log(`🔍 Lotes del insumo #${insumoId}: ${lotes.length}`);
    return of(lotes as any).pipe(delay(300));
  }

  /**
   * Obtener lotes por proveedor
   */
  public getLotesPorProveedor(proveedor: string): Observable<Lote[]> {
    const lotesProv = LOTES_MOCK.filter(l => 
      l.proveedor?.toLowerCase().includes(proveedor.toLowerCase())
    );
    console.log(`🏢 Lotes del proveedor ${proveedor}: ${lotesProv.length}`);
    return of(lotesProv as any).pipe(delay(300));
  }

  // ==========================================
  // ESTADÍSTICAS
  // ==========================================

  /**
   * Obtener estadísticas de lotes
   */
  public getEstadisticas(): Observable<any> {
    const stats = {
      total: LOTES_MOCK.length,
      activos: LOTES_MOCK.filter(l => !l.estaVencido).length,
      vencidos: LOTES_MOCK.filter(l => l.estaVencido).length,
      proximosCaducar: LOTES_MOCK.filter(l => l.diasParaCaducar <= 30).length
    };
    console.log('📊 Estadísticas de lotes:', stats);
    return of(stats).pipe(delay(300));
  }
}
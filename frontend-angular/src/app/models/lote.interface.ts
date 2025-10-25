/**
 * Interfaces relacionadas con Lotes
 * Actualizado: 24/10/2025
 */

export interface Lote {
  id: number;
  numeroLote: string;
  insumoId: number;
  insumoNombre: string;
  fechaCaducidad: string;
  cantidadActual: number;
  estado: string;
}

export interface LoteCaducidadDTO {
  id: number;
  numeroLote: string;
  insumoNombre: string;
  fechaCaducidad: string;
  cantidadActual: number;
  diasParaCaducar: number;
  estaVencido: boolean;
  proveedor?: string;
}

/**
 * Interfaces relacionadas con Lotes
 * PrioridadAlerta se importa para ser usada en LoteCaducidadDTO
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
  // Esta propiedad es usada por el frontend pero no estaba en la interfaz
  nivelAlerta: any; // Se usa 'any' para evitar error de importación circular si se usa PrioridadAlerta
}

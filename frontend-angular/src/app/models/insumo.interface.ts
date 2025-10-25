/**
 * @file insumo.interface.ts
 * @description Mapeo exacto del InsumoDTO del backend.
 */
export interface Insumo {
  id: number; // <-- ¡NECESARIO!
  nombre: string; // <-- ¡NECESARIO!
  codigoCatalogo: string | null;
  unidadMedida: string | null;
  cantidadActual: number | null;
  cantidadMinima: number | null;
  precioUnitario: number | null;
  proveedor: string | null;
  ubicacionAlmacen: string | null;
  fechaCaducidad: string | null;
  lote: string | null;
  categoria: string | null;
  descripcion: string | null;
  estado: string | null;
  consumoPromedioDiario: number | null;
  diasStockRestante: number | null;
  nivelAlerta: string | null;
  createdAt: string | null;
  updatedAt: string | null;
  estaVencido: boolean | null;
  estaProximoAVencer: boolean | null;
  porcentajeStock: number | null;
  valorTotal: number | null;
}

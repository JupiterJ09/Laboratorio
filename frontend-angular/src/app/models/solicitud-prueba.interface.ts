/**
 * Interfaz para las solicitudes de prueba.
 * Coincide con el DTO del backend para Solicitudes de Prueba.
 */
export interface SolicitudPrueba {
  id: number;
  folio: string;
  expediente: string;
  fechaSolicitud: string; // ISO date string
  tipoPrueba: string;
  estado: 'PENDIENTE' | 'EN_PROCESO' | 'COMPLETADA' | 'CANCELADA';
  insumos: { nombre: string; cantidadUsada: number; unidad: string }[];
}
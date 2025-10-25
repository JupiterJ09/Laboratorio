/**
 * Interfaz Alerta - Coincide con AlertaDTO del backend
 * Actualizado: 24/10/2025
 */

export interface Alerta {
  // Identificación
  id: number;
  tipo: TipoAlerta;
  prioridad: PrioridadAlerta;
  titulo: string;
  mensaje: string;

  // Información del insumo relacionado
  insumoId?: number | null;
  insumoNombre?: string | null;
  insumoCodigoCatalogo?: string | null;

  // Información del lote relacionado
  loteId?: number | null;
  loteNumero?: string | null;

  // Estado de la alerta
  leida: boolean;
  fechaCreacion: string; // ISO 8601 string desde el backend
  fechaLectura?: string | null;

  // Usuario
  usuarioDestinatario?: string | null;

  // Datos adicionales
  datosAdicionales?: string | null;

  // Campos calculados (vienen del backend)
  icono?: string;
  color?: string;
  esUrgente?: boolean;
  minutosDesdeCreacion?: number;
}

// Enums para tipos
export type TipoAlerta =
  | 'STOCK_BAJO'
  | 'CADUCIDAD'
  | 'VENCIDO'
  | 'AGOTAMIENTO_PROXIMO'
  | 'SISTEMA'
  | 'OTRO';

export type PrioridadAlerta =
  | 'CRITICA'
  | 'ALTA'
  | 'MEDIA'
  | 'BAJA';

// Helper: Crear alerta vacía (útil para formularios)
export function createEmptyAlerta(): Partial<Alerta> {
  return {
    leida: false,
    fechaCreacion: new Date().toISOString(),
    tipo: 'SISTEMA',
    prioridad: 'MEDIA'
  };
}

// Helper: Verificar si una alerta es reciente (menos de 5 minutos)
export function esAlertaReciente(alerta: Alerta): boolean {
  if (!alerta.minutosDesdeCreacion) return false;
  return alerta.minutosDesdeCreacion < 5;
}

// Helper: Obtener clase CSS según prioridad
export function getClasePrioridad(prioridad: PrioridadAlerta): string {
  const clases: Record<PrioridadAlerta, string> = {
    'CRITICA': 'alerta-critica',
    'ALTA': 'alerta-alta',
    'MEDIA': 'alerta-media',
    'BAJA': 'alerta-baja'
  };
  return clases[prioridad] || 'alerta-default';
}

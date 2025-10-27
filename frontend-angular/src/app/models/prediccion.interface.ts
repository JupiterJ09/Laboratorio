/**
 * @file prediccion.interface.ts
 * @description Define la estructura de la respuesta del endpoint /predecir
 * @description (viene del servicio Flask a través del backend Java).
 */
export interface PrediccionDia {
  dia: number;
  fecha: string; // Formato YYYY-MM-DD
  stock_estimado: number;
}

export interface RespuestaPrediccion {
  insumo_id: number;
  nombre_insumo: string;
  // Otros posibles datos que envíe Flask...
  proyeccion_30_dias: PrediccionDia[];
}

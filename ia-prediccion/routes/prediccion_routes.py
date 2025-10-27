from flask import Blueprint, jsonify
from config import get_connection
from datetime import datetime, timedelta

prediccion_bp = Blueprint('prediccion', __name__)

# ----------------------------
# Endpoint para predecir insumo
# ----------------------------
@prediccion_bp.route('/predecir/<int:insumo_id>', methods=['GET'])
def predecir_insumo(insumo_id):
    conn = None
    cursor = None
    try:
        conn = get_connection()
        cursor = conn.cursor()

        # 1️⃣ Datos base del insumo
        cursor.execute("""
            SELECT id, descripcion AS nombre_insumo, existencia
            FROM insumos_lab
            WHERE id = %s
        """, (insumo_id,))
        insumo = cursor.fetchone()
        if not insumo:
            return jsonify({"error": "Insumo no encontrado"}), 404

        # 2️⃣ Consumo últimos 30 días
        cursor.execute("""
            SELECT SUM(cantidad_egr) AS total_consumo
            FROM salidas_lab
            WHERE id_insumos = %s
              AND fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        """, (insumo_id,))
        consumo = cursor.fetchone()
        total_consumo = consumo["total_consumo"] or 0

        # 3️⃣ Promedio diario
        promedio_diario = total_consumo / 30 if total_consumo > 0 else 0

        # 4️⃣ Proyección 30 días
        proyeccion = []
        stock_actual = insumo["existencia"] or 0
        hoy = datetime.now().date()
        for dia in range(1, 31):
            fecha_futura = hoy + timedelta(days=dia)
            stock_estimado = stock_actual - (promedio_diario * dia)
            stock_estimado = round(stock_estimado, 2) if stock_estimado > 0 else 0
            proyeccion.append({
                "dia": dia,
                "fecha": fecha_futura.isoformat(),
                "stock_estimado": stock_estimado
            })

        # 5️⃣ Riesgo y recomendaciones
        dias_restantes = stock_actual / promedio_diario if promedio_diario > 0 else None
        if total_consumo == 0:
            nivel_riesgo = "SIN CONSUMO RECIENTE"
            recomendacion = "Monitorear"
            cantidad_pedido = 0
        elif dias_restantes and dias_restantes < 7:
            nivel_riesgo = "CRÍTICO"
            recomendacion = "URGENTE: Pedir inmediatamente"
            cantidad_pedido = round(promedio_diario * 60)
        elif dias_restantes and dias_restantes < 30:
            nivel_riesgo = "MODERADO"
            recomendacion = "Programar pedido"
            cantidad_pedido = round(promedio_diario * 45)
        else:
            nivel_riesgo = "NORMAL"
            recomendacion = "Monitorear"
            cantidad_pedido = 0

        # 6️⃣ Respuesta final
        return jsonify({
            "insumo_id": insumo["id"],
            "nombre": insumo["nombre_insumo"],
            "existencia_actual": stock_actual,
            "promedio_diario": round(promedio_diario, 2),
            "dias_restantes": round(dias_restantes, 2) if dias_restantes else None,
            "nivel_riesgo": nivel_riesgo,
            "recomendacion": recomendacion,
            "cantidad_sugerida_pedido": cantidad_pedido,
            "proyeccion_30_dias": proyeccion
        })

    except Exception as e:
        return jsonify({"error": str(e)})
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

# ----------------------------
# NUEVO: Endpoint para precisión del modelo
# ----------------------------
@prediccion_bp.route('/precision', methods=['GET'])
def obtener_precision():
    try:
        # Aquí colocas tu lógica real para calcular precisión del modelo IA
        # Por ahora devolvemos un valor fijo para probar
        precision = 92  # reemplaza con tu cálculo real si lo tienes

        return jsonify({"precision": precision})
    except Exception as e:
        return jsonify({"error": f"No se pudo obtener la precisión: {str(e)}"}), 500

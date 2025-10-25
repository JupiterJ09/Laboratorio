from flask import Blueprint, jsonify
from config import get_connection
import math

modelo_bp = Blueprint('modelo', __name__)

@modelo_bp.route('/modelo/estadisticas', methods=['GET'])
def estadisticas_modelo():
    try:
        conn = get_connection()
        cursor = conn.cursor()  # No usar dictionary=True, ya lo devuelve DictCursor

        cursor.execute("""
            SELECT cantidad_egr
            FROM salidas_lab
            WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        """)
        consumos = cursor.fetchall()

        if not consumos:
            return jsonify({"mensaje": "No hay datos de consumo en los últimos 30 días"})

        n = len(consumos)
        media = sum(c["cantidad_egr"] for c in consumos) / n
        varianza = sum((c["cantidad_egr"] - media) ** 2 for c in consumos) / n
        desviacion = math.sqrt(varianza)
        coef_variacion = (desviacion / media * 100) if media > 0 else 0
        precision_modelo = 100 - coef_variacion

        return jsonify({
            "promedio_consumo_30d": round(media, 2),
            "desviacion_estandar": round(desviacion, 2),
            "coeficiente_variacion": round(coef_variacion, 2),
            "precision_modelo": round(precision_modelo, 2)
        })

    except Exception as e:
        return jsonify({"error": str(e)})
    finally:
        cursor.close()
        conn.close()

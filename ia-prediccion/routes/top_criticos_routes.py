from flask import Blueprint, jsonify
from config import get_connection 

top_criticos_bp = Blueprint('top_criticos', __name__)

@top_criticos_bp.route('/top-criticos', methods=['GET'])
def get_top_criticos():
    try:
        conn = get_connection()
        cursor = conn.cursor()

        cursor.execute("""
            SELECT id, descripcion AS nombre_insumo, existencia
            FROM insumos_lab
            WHERE existencia < 20
        """)
        insumos = cursor.fetchall()

        resultado = []
        for insumo in insumos:
            insumo_id = insumo["id"]

            cursor.execute("""
                SELECT SUM(cantidad_egr) AS total_consumo
                FROM salidas_lab
                WHERE id_insumos = %s
                  AND fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            """, (insumo_id,))
            consumo = cursor.fetchone()
            total_consumo = consumo["total_consumo"] or 0

            promedio_diario = total_consumo / 30 if total_consumo > 0 else 0
            dias_restantes = insumo["existencia"] / promedio_diario if promedio_diario > 0 else None

            resultado.append({
                "insumo_id": insumo_id,
                "nombre": insumo["nombre_insumo"],
                "stock_actual": insumo["existencia"],
                "total_consumo_30d": total_consumo,
                "promedio_diario": round(promedio_diario, 2),
                "dias_restantes": round(dias_restantes, 2) if dias_restantes else None
            })

        resultado = sorted(resultado, key=lambda x: (x["dias_restantes"] or 9999))[:10]

        return jsonify(resultado)

    except Exception as e:
        return jsonify({"error": str(e)})
    finally:
        cursor.close()
        conn.close()

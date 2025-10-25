from flask import Flask, jsonify
from flask_cors import CORS
from config import get_connection
from routes.prediccion_routes import prediccion_bp
from routes.modelo_routes import modelo_bp
from routes.top_criticos_routes import top_criticos_bp

app = Flask(__name__)
CORS(app)

# Registrar Blueprints
app.register_blueprint(prediccion_bp)
app.register_blueprint(modelo_bp)
app.register_blueprint(top_criticos_bp)

@app.route('/')
def index():
    return jsonify({"message": "API del Laboratorio - Predicción de Insumos"})

@app.route('/test-db', methods=['GET'])
def test_db():
    try:
        conn = get_connection()
        conn.close()
        return jsonify({"message": "Conexión a MySQL exitosa ✅"})
    except Exception as e:
        return jsonify({"message": f"No se pudo conectar a MySQL ❌: {e}"}), 500

if __name__ == '__main__':
    app.run(debug=True)

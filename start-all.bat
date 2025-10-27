@echo off
title 🚀 Iniciando Proyecto Laboratorio Completo
color 0A

echo ===========================================
echo    INICIO AUTOMÁTICO DE TODOS LOS SERVICIOS
echo ===========================================
echo.

:: -----------------------------
:: 1. Iniciar MySQL
:: -----------------------------
echo 🧩 Iniciando MySQL...
net start MySQL95
if %errorlevel% neq 0 (
    echo ⚠️  No se pudo iniciar MySQL o ya está en ejecución.
) else (
    echo ✅ MySQL iniciado correctamente.
)
echo.

:: -----------------------------
:: 2. Iniciar Backend Java (Spring Boot)
:: -----------------------------
echo 🚀 Iniciando Spring Boot...
cd backend-java
start cmd /k "mvnw spring-boot:run"
cd ..
echo ✅ Spring Boot ejecutándose en segundo plano.
echo.

:: -----------------------------
:: 3. Iniciar Python (Flask API IA)
:: -----------------------------
echo 🤖 Iniciando servicio de predicción (Flask)...
cd ia-prediccion
start cmd /k "venv\Scripts\activate && python app.py"
cd ..
echo ✅ Flask iniciado correctamente.
echo.

:: -----------------------------
:: 4. Iniciar Angular (Frontend)
:: -----------------------------
echo 🌐 Iniciando Angular...
cd frontend-angular
start cmd /k "npm start"
cd ..
echo ✅ Angular corriendo en http://localhost:4200
echo.

echo ===========================================
echo 💚 TODOS LOS SERVICIOS ESTÁN ARRIBA 💚
echo ===========================================
pause

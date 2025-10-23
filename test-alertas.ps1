# ========================================
# SCRIPT DE PRUEBA - SISTEMA DE ALERTAS
# ========================================
# Autor: Jose Anibal Cabrera Rodas
# Proyecto: Sistema de Inventario con WebSocket
# Descripcion: Script completo para testing de alertas
# ========================================

Write-Host "`n" -NoNewline
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  SCRIPT DE PRUEBA - SISTEMA DE ALERTAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configuracion
$baseUrl = "http://localhost:8081"
$testsPassed = 0
$testsFailed = 0

# Funcion para mostrar resultados
function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [hashtable]$Body = $null
    )
    
    Write-Host "`n[TEST] $Name..." -ForegroundColor Yellow -NoNewline
    
    try {
        if ($Method -eq "GET") {
            $response = Invoke-WebRequest -Uri $Url -Method $Method -ErrorAction Stop
        } else {
            $response = Invoke-WebRequest -Uri $Url -Method $Method -Body $Body -ErrorAction Stop
        }
        
        if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 201) {
            Write-Host " PASS" -ForegroundColor Green
            $script:testsPassed++
            return $response
        } else {
            Write-Host " FAIL (Status: $($response.StatusCode))" -ForegroundColor Red
            $script:testsFailed++
            return $null
        }
    } catch {
        Write-Host " FAIL (Error: $($_.Exception.Message))" -ForegroundColor Red
        $script:testsFailed++
        return $null
    }
}

# ========================================
# FASE 1: VERIFICAR SERVIDOR
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 1: VERIFICAR SERVIDOR" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

$healthResponse = Test-Endpoint `
    -Name "Health Check del servidor" `
    -Url "$baseUrl/api/alertas/health"

if ($healthResponse) {
    Write-Host "   Servidor funcionando correctamente" -ForegroundColor Gray
}

# ========================================
# FASE 2: CRUD DE ALERTAS
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 2: CRUD DE ALERTAS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

# Listar todas las alertas
$alertasResponse = Test-Endpoint `
    -Name "Listar todas las alertas" `
    -Url "$baseUrl/api/alertas"

if ($alertasResponse) {
    $alertas = $alertasResponse.Content | ConvertFrom-Json
    Write-Host "   Total de alertas: $($alertas.Count)" -ForegroundColor Gray
}

# Listar alertas no leidas
$noLeidasResponse = Test-Endpoint `
    -Name "Listar alertas no leidas" `
    -Url "$baseUrl/api/alertas/no-leidas"

if ($noLeidasResponse) {
    $noLeidas = $noLeidasResponse.Content | ConvertFrom-Json
    Write-Host "   Alertas no leidas: $($noLeidas.Count)" -ForegroundColor Gray
}

# Alertas urgentes
$urgentesResponse = Test-Endpoint `
    -Name "Listar alertas urgentes" `
    -Url "$baseUrl/api/alertas/urgentes"

# Estadisticas
$statsResponse = Test-Endpoint `
    -Name "Obtener estadisticas" `
    -Url "$baseUrl/api/alertas/estadisticas"

if ($statsResponse) {
    $stats = $statsResponse.Content | ConvertFrom-Json
    Write-Host "   Estadisticas:" -ForegroundColor Gray
    Write-Host "      - Total no leidas: $($stats.totalNoLeidas)" -ForegroundColor Gray
    Write-Host "      - Criticas: $($stats.criticas)" -ForegroundColor Gray
    Write-Host "      - Altas: $($stats.altas)" -ForegroundColor Gray
    Write-Host "      - Medias: $($stats.medias)" -ForegroundColor Gray
    Write-Host "      - Bajas: $($stats.bajas)" -ForegroundColor Gray
}

# ========================================
# FASE 3: WEBSOCKET ENDPOINTS
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 3: WEBSOCKET ENDPOINTS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

# Test WebSocket - Alerta de prueba
$urlTest = "$baseUrl/api/websocket/test"
$urlTest += "?titulo=TestScript"
$urlTest += "&tipo=STOCK_BAJO"
$urlTest += "&prioridad=MEDIA"

$wsTestResponse = Test-Endpoint `
    -Name "WebSocket - Alerta de prueba" `
    -Url $urlTest `
    -Method "POST"

if ($wsTestResponse) {
    Write-Host "   $($wsTestResponse.Content)" -ForegroundColor Gray
}

# Sincronizar alertas
$wsSyncResponse = Test-Endpoint `
    -Name "WebSocket - Sincronizar alertas" `
    -Url "$baseUrl/api/websocket/alertas/sync"

if ($wsSyncResponse) {
    Write-Host "   $($wsSyncResponse.Content)" -ForegroundColor Gray
}

# Broadcast
$urlBroadcast = "$baseUrl/api/websocket/broadcast"
$urlBroadcast += "?mensaje=TestBroadcast"
$urlBroadcast += "&prioridad=BAJA"

$wsBroadcastResponse = Test-Endpoint `
    -Name "WebSocket - Broadcast" `
    -Url $urlBroadcast

if ($wsBroadcastResponse) {
    Write-Host "   $($wsBroadcastResponse.Content)" -ForegroundColor Gray
}

# Emergencia
$urlEmergencia = "$baseUrl/api/websocket/emergencia"
$urlEmergencia += "?titulo=TestEmergencia"
$urlEmergencia += "&mensaje=PruebaScript"

$wsEmergenciaResponse = Test-Endpoint `
    -Name "WebSocket - Emergencia" `
    -Url $urlEmergencia `
    -Method "POST"

if ($wsEmergenciaResponse) {
    Write-Host "   $($wsEmergenciaResponse.Content)" -ForegroundColor Gray
}

# ========================================
# FASE 4: VERIFICACION AUTOMATICA
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 4: VERIFICACION AUTOMATICA" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

# Verificar todo el sistema
$verifyResponse = Test-Endpoint `
    -Name "Verificar y generar alertas" `
    -Url "$baseUrl/api/alertas/verificar-y-generar" `
    -Method "POST"

if ($verifyResponse) {
    $verifyData = $verifyResponse.Content | ConvertFrom-Json
    Write-Host "   Resultados:" -ForegroundColor Gray
    Write-Host "      - Mensaje: $($verifyData.mensaje)" -ForegroundColor Gray
    Write-Host "      - Alertas generadas: $($verifyData.alertasGeneradas)" -ForegroundColor Gray
}

# Verificar stock bajo
$stockResponse = Test-Endpoint `
    -Name "Verificar stock bajo" `
    -Url "$baseUrl/api/alertas/verificar-stock-bajo" `
    -Method "POST"

# Verificar caducidad
$caducidadResponse = Test-Endpoint `
    -Name "Verificar caducidad" `
    -Url "$baseUrl/api/alertas/verificar-caducidad" `
    -Method "POST"

# Verificar vencidos
$vencidosResponse = Test-Endpoint `
    -Name "Verificar vencidos" `
    -Url "$baseUrl/api/alertas/verificar-vencidos" `
    -Method "POST"

# ========================================
# FASE 5: BUSQUEDAS Y FILTROS
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 5: BUSQUEDAS Y FILTROS" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

# Buscar por tipo
Test-Endpoint `
    -Name "Buscar por tipo: STOCK_BAJO" `
    -Url "$baseUrl/api/alertas/tipo/STOCK_BAJO"

# Buscar por prioridad
Test-Endpoint `
    -Name "Buscar por prioridad: CRITICA" `
    -Url "$baseUrl/api/alertas/prioridad/CRITICA"

# Alertas de hoy
Test-Endpoint `
    -Name "Alertas de hoy" `
    -Url "$baseUrl/api/alertas/hoy"

# ========================================
# FASE 6: CREAR ALERTA PERSONALIZADA
# ========================================
Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "  FASE 6: CREAR ALERTA PERSONALIZADA" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta

$bodyJson = @{
    tipo = "STOCK_BAJO"
    prioridad = "ALTA"
    titulo = "Alerta creada por script"
    mensaje = "Esta alerta fue creada desde el script de prueba PowerShell"
    insumoId = $null
    loteId = $null
} | ConvertTo-Json

try {
    Write-Host "`n[TEST] Crear alerta personalizada..." -ForegroundColor Yellow -NoNewline
    $customResponse = Invoke-RestMethod `
        -Uri "$baseUrl/api/alertas/crear-personalizada" `
        -Method POST `
        -Body $bodyJson `
        -ContentType "application/json"
    
    Write-Host " PASS" -ForegroundColor Green
    $script:testsPassed++
    Write-Host "   Alerta creada con ID: $($customResponse.id)" -ForegroundColor Gray
} catch {
    Write-Host " FAIL" -ForegroundColor Red
    $script:testsFailed++
}

# ========================================
# RESUMEN FINAL
# ========================================
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE PRUEBAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Tests pasados: $testsPassed" -ForegroundColor Green
Write-Host "Tests fallidos: $testsFailed" -ForegroundColor Red
Write-Host ""

$total = $testsPassed + $testsFailed
if ($total -gt 0) {
    $percentage = [math]::Round(($testsPassed / $total) * 100, 2)

    if ($testsFailed -eq 0) {
        Write-Host "TODOS LOS TESTS PASARON! ($percentage%)" -ForegroundColor Green
    } elseif ($percentage -ge 80) {
        Write-Host "La mayoria de tests pasaron ($percentage%)" -ForegroundColor Yellow
    } else {
        Write-Host "Muchos tests fallaron ($percentage%)" -ForegroundColor Red
    }
}

Write-Host "`n========================================`n" -ForegroundColor Cyan

# ========================================
# INFORMACION ADICIONAL
# ========================================
Write-Host "Informacion adicional:" -ForegroundColor Cyan
Write-Host "   - Para ver alertas en tiempo real, abre websocket-client-cloudflare.html" -ForegroundColor Gray
Write-Host "   - Las tareas programadas verifican automaticamente cada cierto tiempo" -ForegroundColor Gray
Write-Host "   - Revisa los logs del servidor Spring Boot para mas detalles" -ForegroundColor Gray
Write-Host ""
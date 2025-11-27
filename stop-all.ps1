# Script para detener todo el sistema de carrito de compras
# Puede ejecutarse desde cualquier ubicacion
# Uso: .\stop-all.ps1

$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "Deteniendo Sistema de Carrito de Compras..." -ForegroundColor Red
Write-Host ""

# Detener Keycloak
Write-Host "Deteniendo Keycloak (Identity Server)..." -ForegroundColor Yellow
Set-Location C:\Github\identity-server
docker-compose down -v 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Keycloak detenido" -ForegroundColor Green
} else {
    Write-Host "AVISO Keycloak ya estaba detenido o no se encontro" -ForegroundColor Yellow
}

Write-Host ""

# Detener Redis
Write-Host "Deteniendo Redis..." -ForegroundColor Yellow
Set-Location C:\Github\back-shopping-cart
docker-compose down -v 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Redis detenido" -ForegroundColor Green
} else {
    Write-Host "AVISO Redis ya estaba detenido o no se encontro" -ForegroundColor Yellow
}

Write-Host ""

# Detener RabbitMQ
Write-Host "Deteniendo RabbitMQ..." -ForegroundColor Yellow
Set-Location C:\Github\rabbitmq
docker-compose down -v 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK RabbitMQ detenido" -ForegroundColor Green
} else {
    Write-Host "AVISO RabbitMQ ya estaba detenido o no se encontro" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host "OK Contenedores Docker detenidos" -ForegroundColor Green
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "RECUERDA cerrar manualmente las terminales:" -ForegroundColor Cyan
Write-Host "   - Backend (Spring Boot) - Ctrl+C" -ForegroundColor Gray
Write-Host "   - Frontend Login - Ctrl+C" -ForegroundColor Gray
Write-Host "   - Frontend Shopping Cart - Ctrl+C" -ForegroundColor Gray
Write-Host ""
Write-Host "O simplemente cierra las ventanas de PowerShell" -ForegroundColor Yellow
Write-Host ""

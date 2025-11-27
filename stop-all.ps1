# Script para detener todo el sistema
# Uso: .\stop-all.ps1

Write-Host "🛑 Deteniendo Sistema de Carrito de Compras..." -ForegroundColor Red
Write-Host ""

# Detener Redis
Write-Host "📦 Deteniendo Redis..." -ForegroundColor Yellow
Set-Location C:\Github\back-shopping-cart
docker-compose down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Redis detenido correctamente" -ForegroundColor Green
} else {
    Write-Host "⚠️  Error al detener Redis (puede que ya estuviera detenido)" -ForegroundColor Yellow
}

Write-Host ""

# Detener RabbitMQ
Write-Host "📦 Deteniendo RabbitMQ..." -ForegroundColor Yellow
Set-Location C:\Github\rabbitmq
docker-compose down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ RabbitMQ detenido correctamente" -ForegroundColor Green
} else {
    Write-Host "⚠️  Error al detener RabbitMQ (puede que ya estuviera detenido)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "ℹ️  Recuerda detener manualmente:" -ForegroundColor Cyan
Write-Host "   - Terminal del Backend (Ctrl+C)" -ForegroundColor Gray
Write-Host "   - Terminal del Frontend (Ctrl+C)" -ForegroundColor Gray
Write-Host ""
Write-Host "✅ Sistema detenido" -ForegroundColor Green

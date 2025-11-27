# Script para levantar todo el sistema de carrito de compras
# Uso: .\start-all.ps1

Write-Host "🚀 Iniciando Sistema de Carrito de Compras..." -ForegroundColor Cyan
Write-Host ""

# Paso 1a: Levantar Redis
Write-Host "📦 Paso 1/5: Levantando Redis con Docker..." -ForegroundColor Yellow
Set-Location C:\Github\back-shopping-cart
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Redis iniciado correctamente" -ForegroundColor Green
    Write-Host "   - Redis: localhost:6379" -ForegroundColor Gray
} else {
    Write-Host "❌ Error al iniciar Redis" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Paso 1b: Levantar RabbitMQ
Write-Host "📦 Paso 2/5: Levantando RabbitMQ con Docker..." -ForegroundColor Yellow
Set-Location C:\Github\rabbitmq
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ RabbitMQ iniciado correctamente" -ForegroundColor Green
    Write-Host "   - RabbitMQ: localhost:5672" -ForegroundColor Gray
    Write-Host "   - RabbitMQ Management: http://localhost:15672 (guest/guest)" -ForegroundColor Gray
} else {
    Write-Host "❌ Error al iniciar RabbitMQ" -ForegroundColor Red
    exit 1
}

Write-Host ""
Start-Sleep -Seconds 3

# Paso 2: Iniciar Backend (en nueva terminal)
Write-Host "☕ Paso 3/5: Iniciando Backend (Spring Boot)..." -ForegroundColor Yellow
Write-Host "   Se abrirá una nueva terminal para el backend..." -ForegroundColor Gray

$backendScript = @"
Write-Host '☕ Iniciando Backend Spring Boot...' -ForegroundColor Cyan
Set-Location C:\Github\back-shopping-cart
java -jar gradle/wrapper/gradle-wrapper.jar bootRun
"@

Start-Process powershell -ArgumentList "-NoExit", "-Command", $backendScript

Write-Host "✅ Backend iniciándose en nueva terminal" -ForegroundColor Green
Write-Host "   Espera 30-40 segundos a que inicie completamente..." -ForegroundColor Gray
Write-Host ""
Start-Sleep -Seconds 5

# Paso 3: Iniciar Frontend Login (puerto 4200)
Write-Host "🔐 Paso 4/5: Iniciando Frontend Login (Angular)..." -ForegroundColor Yellow
Write-Host "   Se abrirá una nueva terminal para front-login..." -ForegroundColor Gray

$frontendLoginScript = @"
Write-Host '🔐 Iniciando Frontend Login en puerto 4200...' -ForegroundColor Cyan
Set-Location C:\Github\front-login
npm start
"@

Start-Process powershell -ArgumentList "-NoExit", "-Command", $frontendLoginScript

Write-Host "✅ Frontend Login iniciándose en nueva terminal (puerto 4200)" -ForegroundColor Green
Write-Host ""
Start-Sleep -Seconds 3

# Paso 4: Iniciar Frontend Shopping Cart (puerto 4201)
Write-Host "🛒 Paso 5/5: Iniciando Frontend Shopping Cart (Angular)..." -ForegroundColor Yellow
Write-Host "   Se abrirá una nueva terminal para front-shopping-cart..." -ForegroundColor Gray

$frontendCartScript = @"
Write-Host '🛒 Iniciando Frontend Shopping Cart en puerto 4201...' -ForegroundColor Cyan
Set-Location C:\Github\front-shopping-cart
npm start
"@

Start-Process powershell -ArgumentList "-NoExit", "-Command", $frontendCartScript

Write-Host "✅ Frontend Shopping Cart iniciándose en nueva terminal (puerto 4201)" -ForegroundColor Green
Write-Host ""

# Resumen
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "✨ SISTEMA INICIADO" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host ""
Write-Host "📌 URLs disponibles:" -ForegroundColor White
Write-Host "   Frontend Login:  http://localhost:4200" -ForegroundColor Cyan
Write-Host "   Frontend Cart:   http://localhost:4201" -ForegroundColor Cyan
Write-Host "   Backend API:     http://localhost:8080/shopping-cart/api/cart" -ForegroundColor Cyan
Write-Host "   Swagger Docs:    http://localhost:8080/shopping-cart/docs" -ForegroundColor Cyan
Write-Host "   RabbitMQ UI:     http://localhost:15672" -ForegroundColor Cyan
Write-Host ""
Write-Host "⏳ Espera 40 segundos antes de acceder a las aplicaciones..." -ForegroundColor Yellow
Write-Host ""
Write-Host "🧪 Para probar:" -ForegroundColor White
Write-Host "   1. Login: http://localhost:4200 (autenticar usuario)" -ForegroundColor Gray
Write-Host "   2. Catálogo: http://localhost:4201 (ver productos)" -ForegroundColor Gray
Write-Host "   3. Agrega productos al carrito" -ForegroundColor Gray
Write-Host "   4. Ve al carrito y haz checkout" -ForegroundColor Gray
Write-Host ""
Write-Host "🔍 Verificar Redis:" -ForegroundColor White
Write-Host '   docker exec -it shopping-cart-redis redis-cli -a securepassword' -ForegroundColor Gray
Write-Host '   KEYS *' -ForegroundColor Gray
Write-Host ""
Write-Host "🛑 Para detener todo:" -ForegroundColor White
Write-Host "   - Cierra las terminales del backend y frontends (Ctrl+C)" -ForegroundColor Gray
Write-Host "   - Ejecuta: .\stop-all.ps1" -ForegroundColor Gray
Write-Host ""

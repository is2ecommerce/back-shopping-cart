# Script maestro para levantar todo el sistema de carrito de compras
# Puede ejecutarse desde cualquier ubicacion
# Uso: .\start-all.ps1

$ErrorActionPreference = "Continue"

Write-Host "Iniciando Sistema Completo de Carrito de Compras..." -ForegroundColor Cyan
Write-Host ""

# Paso 1: Levantar Keycloak (Identity Server)
Write-Host "Paso 1/6: Levantando Keycloak (Identity Server)..." -ForegroundColor Yellow
Set-Location C:\Github\identity-server
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Keycloak iniciado correctamente" -ForegroundColor Green
    Write-Host "   - Keycloak: http://localhost:8080" -ForegroundColor Gray
    Write-Host "   - Admin Console: http://localhost:8080/admin (admin/admin)" -ForegroundColor Gray
} else {
    Write-Host "ERROR al iniciar Keycloak" -ForegroundColor Red
    exit 1
}

Write-Host ""
Start-Sleep -Seconds 2

# Paso 2: Levantar Redis
Write-Host "Paso 2/6: Levantando Redis..." -ForegroundColor Yellow
Set-Location C:\Github\back-shopping-cart
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK Redis iniciado correctamente (localhost:6379)" -ForegroundColor Green
} else {
    Write-Host "ERROR al iniciar Redis" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Paso 3: Levantar RabbitMQ
Write-Host "Paso 3/6: Levantando RabbitMQ..." -ForegroundColor Yellow
Set-Location C:\Github\rabbitmq
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK RabbitMQ iniciado correctamente" -ForegroundColor Green
    Write-Host "   - AMQP: localhost:5672" -ForegroundColor Gray
    Write-Host "   - Management UI: http://localhost:15672 (guest/guest)" -ForegroundColor Gray
} else {
    Write-Host "ERROR al iniciar RabbitMQ" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Esperando 5 segundos para que los contenedores terminen de iniciar..." -ForegroundColor Gray
Start-Sleep -Seconds 5

# Paso 4: Iniciar Backend Spring Boot
Write-Host "Paso 4/6: Iniciando Backend (Spring Boot)..." -ForegroundColor Yellow
Write-Host "   Abriendo nueva terminal para el backend..." -ForegroundColor Gray

$backendScript = @"
Write-Host '===================================================' -ForegroundColor Cyan
Write-Host 'BACKEND - Spring Boot Shopping Cart' -ForegroundColor Cyan
Write-Host '===================================================' -ForegroundColor Cyan
Write-Host ''
Set-Location C:\Github\back-shopping-cart
Write-Host 'Iniciando aplicacion Spring Boot...' -ForegroundColor Yellow
Write-Host 'URL: http://localhost:8080/shopping-cart' -ForegroundColor Gray
Write-Host 'Swagger: http://localhost:8080/shopping-cart/docs' -ForegroundColor Gray
Write-Host ''
java -jar gradle/wrapper/gradle-wrapper.jar bootRun
"@

Start-Process powershell -ArgumentList @("-NoExit", "-Command", $backendScript)

Write-Host "OK Backend iniciandose en nueva terminal (puerto 8080)" -ForegroundColor Green
Write-Host "   Espera 30-40 segundos..." -ForegroundColor Gray
Write-Host ""
Start-Sleep -Seconds 5

# Paso 5: Iniciar Frontend Login
Write-Host "Paso 5/6: Iniciando Frontend Login..." -ForegroundColor Yellow
Write-Host "   Abriendo nueva terminal para front-login..." -ForegroundColor Gray

$frontendLoginScript = @"
Write-Host '===================================================' -ForegroundColor Magenta
Write-Host 'FRONTEND LOGIN - Autenticacion Keycloak' -ForegroundColor Magenta
Write-Host '===================================================' -ForegroundColor Magenta
Write-Host ''
Set-Location C:\Github\front-login
Write-Host 'Iniciando servidor de desarrollo Angular...' -ForegroundColor Yellow
Write-Host 'URL: http://localhost:4200' -ForegroundColor Gray
Write-Host ''
npm start
"@

Start-Process powershell -ArgumentList @("-NoExit", "-Command", $frontendLoginScript)

Write-Host "OK Frontend Login iniciandose (puerto 4200)" -ForegroundColor Green
Write-Host ""
Start-Sleep -Seconds 3

# Paso 6: Iniciar Frontend Shopping Cart
Write-Host "Paso 6/6: Iniciando Frontend Shopping Cart..." -ForegroundColor Yellow
Write-Host "   Abriendo nueva terminal para front-shopping-cart..." -ForegroundColor Gray

$frontendCartScript = @"
Write-Host '===================================================' -ForegroundColor Green
Write-Host 'FRONTEND SHOPPING CART - Catalogo y Carrito' -ForegroundColor Green
Write-Host '===================================================' -ForegroundColor Green
Write-Host ''
Set-Location C:\Github\front-shopping-cart
Write-Host 'Iniciando servidor de desarrollo Angular...' -ForegroundColor Yellow
Write-Host 'URL: http://localhost:4201' -ForegroundColor Gray
Write-Host ''
npm start
"@

Start-Process powershell -ArgumentList @("-NoExit", "-Command", $frontendCartScript)

Write-Host "OK Frontend Shopping Cart iniciandose (puerto 4201)" -ForegroundColor Green
Write-Host ""
Start-Sleep -Seconds 2

# Resumen final
Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host "                  SISTEMA COMPLETAMENTE INICIADO                       " -ForegroundColor Green
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "APLICACIONES:" -ForegroundColor White
Write-Host ""
Write-Host "   Keycloak          http://localhost:8080 (admin/admin)" -ForegroundColor Blue
Write-Host "   Backend API       http://localhost:8080/shopping-cart/api" -ForegroundColor Cyan
Write-Host "   API Docs          http://localhost:8080/shopping-cart/docs" -ForegroundColor Cyan
Write-Host "   Login/Auth        http://localhost:4200" -ForegroundColor Magenta
Write-Host "   Shopping Cart     http://localhost:4201" -ForegroundColor Green
Write-Host ""
Write-Host "INFRAESTRUCTURA:" -ForegroundColor White
Write-Host ""
Write-Host "   Redis             localhost:6379" -ForegroundColor Red
Write-Host "   RabbitMQ          localhost:5672" -ForegroundColor Yellow
Write-Host "   RabbitMQ UI       http://localhost:15672 (guest/guest)" -ForegroundColor Yellow
Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANTE: Espera 60-90 segundos antes de usar las aplicaciones" -ForegroundColor Yellow
Write-Host "            (Keycloak tarda ~30s, Backend ~40s, Frontends ~20s)" -ForegroundColor Yellow
Write-Host ""
Write-Host "FLUJO DE PRUEBA:" -ForegroundColor White
Write-Host "   1. Espera a que todas las terminales muestren 'started' o 'compiled successfully'" -ForegroundColor Gray
Write-Host "   2. Configura Keycloak: http://localhost:8080/admin" -ForegroundColor Gray
Write-Host "   3. Login: http://localhost:4200" -ForegroundColor Gray
Write-Host "   4. Catalogo: http://localhost:4201" -ForegroundColor Gray
Write-Host "   5. Agrega productos al carrito" -ForegroundColor Gray
Write-Host "   6. Haz checkout" -ForegroundColor Gray
Write-Host ""
Write-Host "VERIFICAR REDIS:" -ForegroundColor White
Write-Host "   docker exec -it shopping-cart-redis redis-cli -a securepassword" -ForegroundColor Gray
Write-Host "   KEYS *" -ForegroundColor Gray
Write-Host ""
Write-Host "DETENER SISTEMA:" -ForegroundColor White
Write-Host "   .\stop-all.ps1" -ForegroundColor Gray
Write-Host "   (o cierra las 3 terminales con Ctrl+C)" -ForegroundColor Gray
Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host ""

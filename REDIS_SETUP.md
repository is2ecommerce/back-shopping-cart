# 🚀 Guía de Inicio Rápido - Sistema de Carrito con Redis

## 📋 Requisitos Previos
- Docker Desktop instalado y corriendo
- Java 21+ (para backend)
- Node.js 18+ (para frontend)

---

## 🗄️ Paso 1: Levantar Infraestructura Docker

### 1.1 Levantar Keycloak (Identity Server)
```powershell
cd C:\Github\identity-server
docker-compose up -d
```

### 1.2 Levantar Redis
```powershell
cd C:\Github\back-shopping-cart
docker-compose up -d
```

### 1.3 Levantar RabbitMQ
```powershell
cd C:\Github\rabbitmq
docker-compose up -d
```

### Verificar que estén corriendo:
```powershell
docker ps
```

Deberías ver:
- `keycloak` y `postgres` (puertos 8080, 5432)
- `shopping-cart-redis` (puerto 6379)
- `rabbitmq` (puerto 5672 y 15672)

### Acceder a interfaces:
- **Keycloak**: http://localhost:8080 (admin/admin)
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

---

## ☕ Paso 2: Iniciar el Backend (Spring Boot)

```powershell
cd C:\Github\back-shopping-cart
.\gradlew.bat bootRun
```

**Backend estará en:** http://localhost:8081/shopping-cart
**Nota:** Usa puerto 8081 porque Keycloak usa el 8080

### Verificar que funciona:
- Swagger UI: http://localhost:8081/shopping-cart/docs
- Health check: http://localhost:8081/shopping-cart/actuator/health

---

## 🎨 Paso 3: Iniciar los Frontends (Angular)

### 3.1 Frontend Login (puerto 4200)
```powershell
cd C:\Github\front-login
npm start
```

**Frontend Login estará en:** http://localhost:4200

### 3.2 Frontend Shopping Cart (puerto 4201)
```powershell
cd C:\Github\front-shopping-cart
npm start
```

**Frontend Cart estará en:** http://localhost:4201

---

## 🧪 Paso 4: Probar el Flujo Completo

### 4.1 Autenticación
1. Abre http://localhost:4200 (Frontend Login)
2. Ingresa credenciales (si Keycloak está configurado)

### 4.2 Ver el Catálogo
1. Abre http://localhost:4201 (Frontend Shopping Cart)
2. Verás 10 productos mock

### 4.2 Agregar al Carrito
1. Click en "Add to Cart" en cualquier producto
2. El backend guardará en Redis con tu userId (extraído del JWT)

### 4.3 Ver el Carrito
1. Click en el botón del carrito (esquina superior derecha)
2. O navega a http://localhost:4201/cart

### 4.4 Verificar Redis (Opcional)
```powershell
# Conectarse a Redis
docker exec -it shopping-cart-redis redis-cli -a securepassword

# Listar todas las claves
KEYS *

# Ver un carrito específico (reemplaza {userId} con un UUID real)
GET "cart:{userId}"

# Salir de Redis
exit
```

### 4.5 Checkout
1. En el carrito, click en "Proceed to Checkout"
2. Completa el formulario
3. El backend:
   - Verifica stock
   - Envía evento a RabbitMQ
   - Limpia el carrito de Redis

---

## 🔍 Verificar que Redis Funciona

### Ver logs del backend:
```powershell
# Si estás usando gradlew bootRun, los logs aparecerán en la consola
# Busca líneas como:
# - "Checkout event sent: ..."
# - Conexión a Redis exitosa
```

### Ver mensajes en RabbitMQ:
1. Abre http://localhost:15672
2. Login: guest/guest
3. Ve a "Queues" y verás los mensajes de checkout

---

## 🛠️ Solución de Problemas

### Redis no se conecta:
```powershell
# Verifica que el contenedor esté corriendo
docker ps | Select-String redis

# Ver logs de Redis
docker logs shopping-cart-redis

# Reiniciar Redis
docker-compose restart redis
```

### Backend no inicia:
```powershell
# Verifica que Redis esté corriendo ANTES de iniciar el backend
docker ps

# Verifica la configuración en application.yml
# Redis debe estar en localhost:6379 con password: securepassword
```

### Frontend no conecta con backend:
- Verifica que el backend esté en http://localhost:8080/shopping-cart
- Revisa la consola del navegador (F12) para errores CORS
- Verifica environment.ts: `apiUrl: 'http://localhost:8080/shopping-cart/api'`

---

## 📊 Arquitectura del Sistema

```
┌─────────────────┐         ┌─────────────────┐
│   Front-Login   │  Auth   │  Front-Shopping │
│  (Port: 4200)   │◄────────┤  Cart (Angular) │
└─────────────────┘         │  Port: 4201     │
                            └────────┬────────┘
                                     │ HTTP
                                     ▼
                            ┌─────────────────┐
                            │  Back-Shopping  │
                            │  Cart (Spring)  │
                            │  Port: 8080     │
                            └────────┬────────┘
                                     │
                        ┌────────────┴────────────┐
                        ▼                         ▼
                 ┌─────────────┐         ┌──────────────┐
                 │    Redis    │         │  RabbitMQ    │
                 │  Port: 6379 │         │  Port: 5672  │
                 └─────────────┘         └──────────────┘
```

### Flujo de Datos:
1. **Usuario agrega producto**: Frontend → Backend → Redis (guarda carrito)
2. **Usuario ve carrito**: Frontend → Backend → Redis (lee carrito)
3. **Usuario hace checkout**: Frontend → Backend → Redis (lee) → RabbitMQ (envía evento) → Redis (borra)

---

## 🎯 Datos Mock Sincronizados

Los siguientes UUIDs están sincronizados entre frontend y backend:

| Producto                    | UUID                                   | Precio  | Stock |
|-----------------------------|----------------------------------------|---------|-------|
| Wireless Bluetooth Headphones | 123e4567-e89b-12d3-a456-426614174000 | $89.99  | 15    |
| Premium Cotton T-Shirt       | 123e4567-e89b-12d3-a456-426614174001 | $29.99  | 50    |
| Leather Wallet               | 123e4567-e89b-12d3-a456-426614174002 | $49.99  | 30    |
| Smart Watch Series 8         | 123e4567-e89b-12d3-a456-426614174003 | $299.99 | 8     |
| Running Shoes Ultraboost     | 123e4567-e89b-12d3-a456-426614174004 | $129.99 | 25    |
| Portable Bluetooth Speaker   | 123e4567-e89b-12d3-a456-426614174005 | $79.99  | 20    |
| Stainless Steel Water Bottle | 123e4567-e89b-12d3-a456-426614174006 | $24.99  | 100   |
| Wireless Gaming Mouse        | 123e4567-e89b-12d3-a456-426614174007 | $59.99  | 12    |
| Yoga Mat Premium             | 123e4567-e89b-12d3-a456-426614174008 | $34.99  | 40    |
| Coffee Maker Automatic       | 123e4567-e89b-12d3-a456-426614174009 | $89.99  | 18    |

---

## 🔄 Próximos Pasos (Opcional)

1. **Integrar Keycloak**: Para autenticación real con JWT
2. **Crear microservicio de catálogo**: Reemplazar CatalogClient mock
3. **Persistir órdenes**: Consumir eventos de RabbitMQ y guardar en BD
4. **Dashboard de administración**: Ver carritos activos en Redis

---

## 📝 Notas Importantes

- **Redis es volátil**: Los carritos se pierden si reinicias el contenedor (usa volumenes para persistir)
- **Mock de catálogo**: El backend tiene los mismos productos que el frontend
- **JWT temporal**: El backend extrae userId del JWT, asegúrate de tener Keycloak configurado
- **Sin autenticación**: Por ahora el frontend no envía JWT (agregar interceptor más adelante)

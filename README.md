# 🎮 Ludico - Backend API

Plataforma de gestión de eventos de juegos de mesa con arquitectura de microservicios.

**Creador:** Nitchen Martinez

---

## 📋 Descripción

Ludico permite a los usuarios crear, descubrir y unirse a eventos de juegos de mesa (Magic: The Gathering, Dungeons & Dragons, juegos de mesa, juegos de cartas, etc.). Incluye chat en tiempo real, gestión de participantes y autenticación segura con JWT.

---

## 🏗️ Arquitectura de Microservicios

| Microservicio | Puerto | Función |
|---|---|---|
| **Config Server** | 8888 | Servidor de configuración centralizada |
| **Eureka** | 8761 | Service Discovery y Health Check |
| **Gateway** | 8000 | API Gateway principal |
| **User Service** | 8050 | Autenticación y gestión de usuarios |
| **Event Service** | 8025 | Gestión de eventos y participantes |
| **Swagger Central** | 8900 | Documentación OpenAPI (Swagger UI) |

---

## 🔧 Tecnologías

- **Java 17**
- **Spring Boot 3.3.12**
- **Spring Cloud 2023.0.5**
- **MySQL 8.0+**
- **JWT (JSON Web Tokens)**
- **Spring Security**
- **WebSocket (STOMP)**

---

## 🚀 Inicio Rápido

### 1. Requisitos Previos

- Java 17 instalado
- MySQL corriendo en `localhost:3306`
- Base de datos `ludicodbts` creada

### 2. Iniciar Servicios

Abre **6 terminales** y ejecuta en orden (espera 15 segundos entre cada una):

```bash
# Terminal 1 - Config Server (PRIMERO)
cd microservice-config
mvn spring-boot:run

# Terminal 2 - Eureka
cd microservice-eureka
mvn spring-boot:run

# Terminal 3 - Gateway
cd microservice-gateway
mvn spring-boot:run

# Terminal 4 - User Service
cd microservice-user
mvn spring-boot:run

# Terminal 5 - Event Service
cd microservice-event
mvn spring-boot:run

# Terminal 6 - Swagger Central
cd microservice-swagger-central
mvn spring-boot:run
```

Espera ~40 segundos a que todos se estabilicen.

---

## 📱 Uso de la API

### Acceso a Swagger
```
http://localhost:8900/swagger-ui.html
```

### 1. Registrar Usuario
```bash
curl -X POST "http://localhost:8000/auth/register?email=user@ludico.com&password=Pass123!&name=User&rol=USER"
```

### 2. Login (obtener JWT)
```bash
curl -X POST "http://localhost:8000/auth/login?email=user@ludico.com&password=Pass123!"
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "email": "user@ludico.com"
}
```

### 3. Usar Token en Endpoints Autenticados
```bash
curl -X GET "http://localhost:8000/api/v1/users/all" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## 🔐 Rutas Principales

### Autenticación (sin JWT)
- `POST /auth/register` - Registrar nuevo usuario
- `POST /auth/login` - Login y obtener JWT

### Usuarios (requiere JWT)
- `GET /api/v1/users/all` - Listar todos los usuarios
- `GET /api/v1/users/{id}` - Obtener usuario por ID

### Eventos (requiere JWT)
- `GET /api/v1/events` - Listar eventos
- `POST /api/v1/events` - Crear evento
- `GET /api/v1/events/{id}` - Obtener evento
- `POST /api/v1/events/{id}/join` - Unirse a evento
- `POST /api/v1/events/{id}/leave` - Salir de evento

---

## 🔑 Características de Seguridad

✅ **Autenticación JWT**
- Tokens con validez de 1 hora
- Extracción automática de claims

✅ **Encriptación de Contraseñas**
- BCrypt hashing con salt
- Validación segura en login

✅ **WebSocket Seguro**
- Validación JWT en conexiones STOMP
- Autorización de participantes

✅ **Configuración Centralizada**
- Secrets en Config Server
- Valores de entorno separados

---

## 🗄️ Base de Datos

```
Host: localhost:3306
Database: ludicodbts
User: root
Password: (configurada en properties)
```

Tablas:
- `users` - Usuarios registrados
- `events` - Eventos creados
- `participants` - Participantes en eventos
- `event_chat` - Mensajes de chat
- `event_images` - Imágenes de eventos

---

## 📋 Estructura del Proyecto

```
Ludico-app-backend/
├── microservice-config/          # Config Server
├── microservice-eureka/          # Eureka Discovery
├── microservice-gateway/         # API Gateway
├── microservice-user/            # User Service
├── microservice-event/           # Event Service
├── microservice-swagger-central/ # Swagger UI
├── README.md                     # Este archivo
└── QUICK_START.md               # Guía rápida
```

---

## 🛠️ Puertos

| Puerto | Servicio |
|---|---|
| 8000 | API Gateway (usar este) |
| 8025 | Event Service |
| 8050 | User Service |
| 8761 | Eureka Dashboard |
| 8888 | Config Server |
| 8900 | Swagger UI |

---

## 📝 Notas Importantes

⚠️ **Auth endpoints usan Query Parameters, NO JSON body:**
```bash
# ✅ CORRECTO
POST /auth/register?email=user@ludico.com&password=Pass123!&name=User&rol=USER

# ❌ INCORRECTO
POST /auth/register
Content-Type: application/json
{"email": "user@ludico.com", "password": "Pass123!"}
```

⚠️ **Siempre usa JWT en Authorization header:**
```bash
Authorization: Bearer <token>
```

⚠️ **Orden de inicio crítico:**
1. Config Server (primero)
2. Eureka
3. Gateway
4. Servicios (User, Event)
5. Swagger Central

---

## 🐛 Solución de Problemas

| Error | Solución |
|---|---|
| Puerto en uso | `Get-Process java \| Stop-Process -Force` |
| 403 Forbidden | Verificar que uses query params en auth |
| 401 Unauthorized | Token expirado o inválido |
| CORS error | Verificar que usas puerto 8000 (Gateway) |

---

## 📞 Contacto

**Desarrollo:** Nitchen Martinez

---

**Última actualización:** 12 de Noviembre de 2025




---## 📌 Project Overview



## 📋 Quick Start (5 minutos)Ludico is a platform that enables people to find and join tabletop gaming events (Magic: The Gathering, Dungeons & Dragons, board games, card games, etc.). Users can create events, discover nearby games, chat in real-time, and manage participants.



### 1️⃣ Iniciar Todos los Servicios**Team:**

- Nitchen Martinez

```powershell- Domingo Velazquez  

# Terminal 1: Config Server (debe ser primero)- Areliz Isla

cd microservice-config

mvn spring-boot:run---



# Terminal 2: Eureka Server## 🏗️ Architecture

cd microservice-eureka

mvn spring-boot:run### Microservices

1. **microservice-config** (Port 8888)

# Terminal 3: Gateway   - Centralized configuration server

cd microservice-gateway   - Stores JWT secrets and environment-specific configs

mvn spring-boot:run   

2. **microservice-eureka** (Port 8761)

# Terminal 4: User Service   - Service registry and discovery

cd microservice-user   - Health checks and load balancing

mvn spring-boot:run

3. **microservice-gateway** (Port 8086)

# Terminal 5: Event Service   - API Gateway routing

cd microservice-event   - Request filtering and routing rules

mvn spring-boot:run

4. **microservice-user** (Port 8050)

# Terminal 6: Swagger Central (OPCIONAL)   - User management (registration, login)

cd microservice-swagger-central   - Authentication with JWT + BCrypt passwords

mvn spring-boot:run   - REST endpoints: `/auth/register`, `/auth/login`

```

5. **microservice-event** (Port 8080)

**Esperar ~40 segundos a que todos inicien.**   - Event CRUD operations

   - Real-time chat via WebSocket (STOMP)

### 2️⃣ Probar la API   - Image upload management

   - Participant management with authorization

```powershell

# REGISTRAR USUARIO6. **microservice-swagger-central** (Port 8099)

Invoke-WebRequest -Uri "http://localhost:8000/auth/register?email=user@ludico.com&password=Pass123!&name=Test User&rol=USER" -Method POST   - Aggregated API documentation

   - OpenAPI 3.0 specs from all services

# LOGIN (obtener JWT token)

$login = Invoke-WebRequest -Uri "http://localhost:8000/auth/login?email=user@ludico.com&password=Pass123!" -Method POST---

$token = ($login.Content | ConvertFrom-Json).token

## 🔐 Security Features Implemented

# USAR TOKEN (endpoints autenticados)

Invoke-WebRequest -Uri "http://localhost:8000/api/v1/users/all" -Method GET `### Phase 1: JWT Authentication ✅

  -Headers @{"Authorization" = "Bearer $token"}- Spring Security with stateless sessions

```- JWT token generation and validation

- User extraction from token claims

---

### Phase 2: Password Security ✅

## 🏗️ Arquitectura- BCrypt password hashing for secure storage

- Salted password validation on login

| Servicio | Puerto | Función |- Registration with hashed passwords

|----------|--------|---------|

| Config Server | 8888 | 🔧 Configuraciones centralizadas |### Phase 3: Centralized Configuration ✅

| Eureka | 8761 | 📍 Service Registry |- Config Server for centralized JWT secret management

| Gateway | 8000 | 🚪 API Gateway (puerto principal) |- Bootstrap configuration in both microservices

| User | 8050 | 👤 Autenticación y usuarios |- Environment-specific configurations

| Event | 8080 | 🎉 Eventos y participantes |

| Swagger | 8900 | 📚 Documentación OpenAPI |### Phase 4: WebSocket Security ✅

- STOMP ChannelInterceptor for JWT validation on WebSocket connections

---- Participant authorization for chat messages

- Server-side userId validation (prevents client-side spoofing)

## 🔑 Endpoints Principales

---

### Autenticación (Sin JWT requerido)

## 🚀 Quick Start

```

POST /auth/register?email=...&password=...&name=...&rol=USER### Prerequisites

POST /auth/login?email=...&password=...- Java 17

```- Maven 3.8.9+

- MySQL 8.0+ (or H2 for testing)

**Respuesta login:**- Port availability: 8050, 8080, 8088, 8761, 8888, 8099

```json

{### Setup

  "token": "eyJhbGciOiJIUzI1NiJ9...",

  "userId": 1,1. **Clone and navigate to project:**

  "email": "user@ludico.com"```bash

}cd "c:\Users\nitch\Desktop\Proyectos Freelance\Ludico-backend\Ludico-app-backend"

``````



### Usuarios (Requiere JWT token)2. **Build all services:**

```bash

```mvn clean install -DskipTests

GET    /api/v1/users/all                    (listar todos)```

GET    /api/v1/users/{id}                   (obtener por ID)

PUT    /api/v1/users/{id}                   (actualizar)3. **Start services in order:**

DELETE /api/v1/users/{id}                   (eliminar)```bash

```# Config Server

cd microservice-config

### Eventos (Requiere JWT token)mvn spring-boot:run



```# Eureka (in new terminal)

GET    /api/v1/events                       (listar)cd microservice-eureka

GET    /api/v1/events/{id}                  (obtener por ID)mvn spring-boot:run

POST   /api/v1/events                       (crear)

PUT    /api/v1/events/{id}                  (actualizar)# Gateway (in new terminal)

DELETE /api/v1/events/{id}                  (eliminar)cd microservice-gateway

mvn spring-boot:run

POST   /api/v1/events/{id}/join             (unirse)

POST   /api/v1/events/{id}/leave            (salir)# User Service (in new terminal)

```cd microservice-user

mvn spring-boot:run

---

# Event Service (in new terminal)

## 📚 Documentación OpenAPI (Swagger)cd microservice-event

mvn spring-boot:run

**URL:** http://localhost:8900/swagger-ui.html

# Swagger Central (in new terminal)

Muestra documentación interactiva de todos los endpoints con posibilidad de probar directamente desde el navegador.cd microservice-swagger-central

mvn spring-boot:run

---```



## 🔐 Autenticación---



- **Método:** JWT (JSON Web Token)## 📚 API Endpoints

- **Password:** Hasheado con BCrypt

- **Validez token:** 1 hora### Authentication (microservice-user)

- **Header requerido:** `Authorization: Bearer <token>`- `POST /auth/register?email=...&password=...&name=...` - Register new user

- `POST /auth/login?email=...&password=...` - Login and get JWT token

---

### Events (microservice-event)

## 🗄️ Base de Datos- `GET /api/events` - List all events

- `POST /api/events` - Create event

- **Type:** MySQL 8.0+- `PUT /api/events/{id}` - Update event

- **Database:** `ludicodbts`- `DELETE /api/events/{id}` - Delete event

- **Host:** localhost:3306- `GET /api/events/{id}` - Get event details

- **User:** root- `POST /api/events/{id}/join` - Join event (requires JWT)

- `POST /api/events/{id}/leave` - Leave event (requires JWT)

---

### WebSocket Chat (microservice-event)

## 🛠️ Tecnologías- **Endpoint:** `ws://localhost:8080/ws-events`

- **Headers:** `Authorization: Bearer <JWT_TOKEN>`

- **Java 17** (JDK)- **Subscribe:** `/topic/chat` - Receive messages

- **Spring Boot 3.3.12**- **Send:** `/app/chat` - Post message

- **Spring Cloud 2023.0.5**  ```json

- **Maven 3.6+**  {

- **MySQL 8.0+**    "eventId": 1,

- **JWT + BCrypt**    "content": "Message text",

- **Eureka Service Discovery**    "imagePath": null

- **Spring Cloud Gateway**  }

  ```

---

### File Upload (microservice-event)

## ⚠️ Notas Importantes- `POST /api/events/{eventId}/images` - Upload event image

- Max size: 5MB per image

### AuthController: Query Parameters- Storage: `uploads/events/{eventId}/`

El endpoint `/auth/register` usa `@RequestParam` (parámetros de query), **NO** JSON body:

### Documentation

✅ **CORRECTO:**- **Swagger UI:** http://localhost:8099/swagger-ui.html

```powershell- **User Service Docs:** http://localhost:8050/doc/swagger-ui/index.html

http://localhost:8000/auth/register?email=test@ludico.com&password=Pass123!&name=Test- **Event Service Docs:** http://localhost:8080/doc/swagger-ui/index.html

```

---

❌ **INCORRECTO:**

```powershell## 🔑 Authentication Flow

# NO usar JSON body

```### Registration & Login

```

### Orden de InicioPOST /auth/register

1. Config Server (8888) - **PRIMERO**├─ Email + Password (plaintext)

2. Eureka (8761)├─ Password hashed with BCrypt

3. Gateway (8000)└─ User saved to database

4. User (8050)

5. Event (8080)POST /auth/login

6. Swagger (8900) - Opcional├─ Email + Password validated

├─ BCrypt.matches(password, hashedPassword)

---├─ JWT token generated with userId + email claims

└─ Token returned to client

## 📁 Documentación Adicional```



- **PARAMETROS_AUTH_QUERY.md** - Detalles de parámetros de autenticación### API Requests

- **TEST_SCRIPTS.ps1** - Scripts de testing```

- **QUICK_START.md** - Inicio rápido adicionalClient Request

- **REPORTE_RESOLUCION_403.md** - Resolución de error 403├─ Header: Authorization: Bearer <token>

- **RESUMEN_SESION_COMPLETA.md** - Resumen de cambios├─ JwtAuthenticationFilter intercepts

├─ Token validated (signature, expiration)

---├─ userId extracted from claims

├─ SecurityContextHolder populated

**Última actualización:** 12 de Noviembre 2025  └─ Request proceeds with authenticated userId

**Status:** ✅ Completamente funcional```


### WebSocket Connection
```
WebSocket CONNECT
├─ Header: Authorization: Bearer <token>
├─ WebSocketChannelInterceptor validates JWT
├─ userId extracted and set in authentication
├─ Participant authorization check
└─ Connection established

Chat Message Send
├─ ChatController receives message
├─ Validates user is event participant
├─ Persists with server-side userId
└─ Message broadcast to all subscribers
```

---

## 📋 Database Schema (Example)

### User Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255) -- BCrypt hashed
);
```

### Event Table
```sql
CREATE TABLE events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  description TEXT,
  game_type VARCHAR(50),
  creator_id BIGINT,
  latitude DOUBLE,
  longitude DOUBLE,
  capacity INT,
  created_at TIMESTAMP,
  FOREIGN KEY (creator_id) REFERENCES users(id)
);
```

### Participant Table
```sql
CREATE TABLE participants (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  event_id BIGINT,
  user_id BIGINT,
  joined_at TIMESTAMP,
  FOREIGN KEY (event_id) REFERENCES events(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  UNIQUE KEY unique_participant (event_id, user_id)
);
```

### Event Chat Table
```sql
CREATE TABLE event_chats (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  event_id BIGINT,
  user_id BIGINT,
  message TEXT,
  image_path VARCHAR(500),
  created_at TIMESTAMP,
  FOREIGN KEY (event_id) REFERENCES events(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 🔒 Configuration Management

### JWT Configuration (centralized in Config Server)

**File:** `microservice-config/src/main/resources/configurations/msvc-user.yml`
```yaml
jwt:
  secret: aW1wb3J0YW50X3NlY3JldF9jaGFuZ2VfdGhpc19pbl9wcm9kdWN0aW9uX3RvX2tSMjU2X2tleQ==
  expiration-ms: 3600000  # 1 hour
```

**Bootstrap Configuration:**
Both microservices include in `application.properties`:
```properties
spring.cloud.config.import=optional:configserver:http://localhost:8888
```

---

## 📖 Documentation

See **[JWT_AUTHENTICATION_GUIDE.md](./JWT_AUTHENTICATION_GUIDE.md)** for detailed security implementation:
- BCrypt password hashing
- JWT token generation and validation
- WebSocket security with STOMP interceptor
- Participant authorization
- Production recommendations

---

## ✅ Build Status

```
Total time: 23.784 s

[INFO] microservice-config ................. SUCCESS ✅
[INFO] microservice-eureka ................. SUCCESS ✅
[INFO] microservice-gateway ................ SUCCESS ✅
[INFO] microservice-event .................. SUCCESS ✅ (WebSocket + Security)
[INFO] microservice-user ................... SUCCESS ✅ (BCrypt + JWT)
[INFO] microservice-swagger-central ....... SUCCESS ✅
```

All modules compiled successfully with all security features implemented.

---

## 🛠️ Technology Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.3.12
- **Cloud:** Spring Cloud 2023.0.5
- **Security:** Spring Security + JJWT 0.11.5
- **Database:** H2 (dev), MySQL 8.0 (prod)
- **API Docs:** SpringDoc OpenAPI 2.6.0
- **Real-time:** Spring WebSocket (STOMP)
- **Build:** Maven 3.8.9+

---

## 📝 License

Proyecto Freelance 2024


- **Productos**
  - URL: [http://localhost:8060/doc/swagger-ui/index.html](http://localhost:8060/doc/swagger-ui/index.html)
- **Ventas**
  - URL: [http://localhost:9090/doc/swagger-ui/index.html](http://localhost:9090/doc/swagger-ui/index.html)

# Documentación Centralizada

- **Swagger Central**
- URL: [http://localhost:8040/swagger-ui/index.html](http://localhost:8040/swagger-ui/index.html)

## Requisitos

- Java
- Maven
- MySQL
- (Opcional) Laragon para entorno local

## Ejecución

1. Clona el repositorio.
2. Crea las bases de datos ejecutando los scripts SQL de DEV y TEST.
3. Levanta el microservicio de configuración centralizada (microservice-config).
4. Levanta Eureka Server (microservice-eureka).
5. Levanta los microservicios que desees probar (user, sale, product, branch).
6. Levanta el gateway (microservice-gateway).
7. (Opcional) Levanta el microservicio centralizador de Swagger (microservice-swagger-central).
8. Accede a los endpoints y a la documentación Swagger usando las URLs listadas arriba.

## Estructura del Proyecto

- `microservice-user`: Gestión de usuarios. Permite crear, consultar, actualizar y eliminar usuarios del sistema. Expone endpoints REST y su propia documentación Swagger.
- `microservice-sale`: Gestión de ventas. Administra las operaciones de ventas, registro de transacciones y consulta de historial de ventas.
- `microservice-product`: Gestión de productos. Permite la administración de productos, incluyendo altas, bajas, modificaciones y consultas.
- `microservice-branch`: Gestión de sucursales. Maneja la información de las sucursales físicas, incluyendo ubicación y datos de contacto.
- `microservice-gateway`: Gateway API. Encargado de enrutar las peticiones a los microservicios correspondientes y aplicar filtros de seguridad, logging, etc.
- `microservice-eureka`: Service discovery. Registro y descubrimiento de microservicios usando Eureka Server.
- `microservice-config`: Configuración centralizada. Provee la configuración externa y centralizada para todos los microservicios usando Spring Cloud Config Server.
- `microservice-swagger-central`: Microservicio dedicado a centralizar y exponer la documentación Swagger de todos los microservicios en una sola interfaz web.


## Maven terminal

- `microservice-user`: mvn install -pl microservice-user || mvn install -pl microservice-user -am -DskipTests
- `microservice-branch`: mvn install -pl microservice-branch || mvn install -pl microservice-branch -am -DskipTests

- mvn clean install -DskipTests


## IMPORT SQL que se eliminaron al integrar los test

- User
```sql
INSERT INTO users (id, name, email, rol) VALUES (1, 'Ana López', 'ana@example.com', 'ADMIN');
INSERT INTO users (id, name, email, rol) VALUES (2, 'Juan Pérez', 'juan@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (3, 'Lucía Gómez', 'lucia@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (4, 'Carlos Ruiz', 'carlos@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (5, 'María Fernández', 'maria@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (6, 'Pedro Salas', 'pedro@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (7, 'Elena Torres', 'elena@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (8, 'David Navas', 'david@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (9, 'Laura Díaz', 'laura@example.com', 'USER');
INSERT INTO users (id, name, email, rol) VALUES (10, 'Mateo Ríos', 'mateo@example.com', 'ADMIN');

```

---

## 📞 Support & Troubleshooting

### Common Issues

**Port already in use:**
```bash
# Find process using port (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Config Server not found:**
- Ensure microservice-config is running on port 8888
- Check: http://localhost:8888/health

**Database connection error:**
- MySQL: Verify instance running and credentials in msvc-user.yml
- H2: Should auto-initialize for event service

**JWT token expired:**
- Default: 1 hour (3600000ms)
- Get new token via `/auth/login`

### Debug Mode
Add to application.properties:
```properties
logging.level.com.acopl=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## 🎯 Future Enhancements

- [ ] Refresh tokens for improved UX
- [ ] Two-factor authentication (2FA)
- [ ] Email verification on registration
- [ ] Event search by location/game type
- [ ] Rating and review system
- [ ] Notifications (email, push)
- [ ] Mobile app (Flutter/React Native)
- [ ] Payment integration (Stripe)
- [ ] Recurring events
- [ ] Event calendar

---

## 📄 License

Proyecto Freelance 2024


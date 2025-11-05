# Nombres Integrantes
#### Nitchen Martinez
#### Domingo Velazquez
#### Areliz Isla

# Ecomarket Microservices

Este proyecto implementa una arquitectura de microservicios para la gestión de un e-commerce, incluyendo usuarios, ventas, productos y sucursales.

Usamos los microservicios con nombres en ingles para respetar la notación y...
aprender inglés(?)

## Base de Datos DEV

```sql
CREATE DATABASE ecomarketdtbs;
```

## Base de Datos TEST
```sql
CREATE DATABASE ecomarketdtbs_test;
--y solo por si acaso
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
```

## Microservicios y Endpoints

- **Usuarios**
  - URL: [http://localhost:8050/api/v1/users](http://localhost:8050/api/v1/users)
  - URL: [http://localhost:8050/api/v2/users](http://localhost:8050/api/v2/users)
- **Ventas**
  - URL: [http://localhost:9090/api/v1/sale](http://localhost:9090/api/v1/sale)
  - URL: [http://localhost:9090/api/v2/sale](http://localhost:9090/api/v2/sale)
- **Productos**
  - URL: [http://localhost:8060/api/v1/product](http://localhost:8060/api/v1/product)
  - URL: [http://localhost:8060/api/v2/product](http://localhost:8060/api/v2/product)
- **Sucursales**
  - URL: [http://localhost:8070/api/v1/branches](http://localhost:8070/api/v1/branches)
  - URL: [http://localhost:8070/api/v2/branches](http://localhost:8070/api/v2/branches)

## Gateway

- URL: [http://localhost:8086](http://localhost:8086)

## Ruta definida para cada microservicio para visualizar la documentación

- **Usuarios**
  - URL: [http://localhost:8050/doc/swagger-ui/index.html](http://localhost:8050/doc/swagger-ui/index.html)
- **Sucursales**
  - URL: [http://localhost:8070/doc/swagger-ui/index.html](http://localhost:8070/doc/swagger-ui/index.html)
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
- Branch
```sql
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Central', 'Avenida Libertador 1234', 'Santiago', 'Chile');
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Norte', 'Rua das Flores 456', 'Santiago', 'Chile');
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Este', 'Calle de la Paz 789', 'Santiago', 'Chile');
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Sur', 'Rue de Rivoli 101', 'Santiago', 'Chile');
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Italia', 'Via Roma 321', 'Santiago', 'Chile');
INSERT INTO branches (name, address, city, country) VALUES ('Sucursal Oeste', 'Calle del Sol 654', 'Santiago', 'Chile');
```

- Product
```sql
INSERT INTO product (id, name, quantity, price) VALUES (1, 'Detergente Natural', 40, 5250);
```

- Sale 
```sql
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (1, '2024-06-01 10:00:00', 150.50, 1,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (2, '2024-06-02 11:30:00', 200.00, 2,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (3, '2024-06-03 14:15:00', 99.99, 3,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (4, '2024-06-04 16:45:00', 300.00, 4,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (5, '2024-06-05 09:00:00', 120.75, 5,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (6, '2024-06-06 13:30:00', 250.00, 6,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (7, '2024-06-07 15:00:00', 175.25, 7,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (8, '2024-06-08 12:20:00', 80.00, 8,1);
INSERT INTO sales (id, date_time, total, client_id, product_id) VALUES  (9, '2024-06-09 17:10:00', 220.50, 9,1);
```

![Ecomarket drawio](https://github.com/user-attachments/assets/0c0f2a14-3ab4-487c-809f-272082edeb09)

# NovaBank Microservices

## Descripción
NovaBank Microservices es la evolución del sistema bancario NovaBank hacia una arquitectura basada en **microservicios síncronos** utilizando Spring Cloud y Spring Boot.

El sistema divide los dominios de negocio en servicios independientes para mejorar la escalabilidad, mantenibilidad y despliegue desacoplado.

Los microservicios principales del sistema son:
- **customer-service** → gestión de clientes.
- **account-service** → gestión de cuentas y movimientos.
- **operation-service** → depósitos, retiros y transferencias.

Además, el ecosistema incluye servicios de infraestructura:
- **Eureka Server** → descubrimiento de servicios.
- **Config Server** → configuración centralizada.
- **API Gateway** → punto de entrada único.
- **Auth Server** → autenticación JWT/OAuth2.

---

# Arquitectura

La arquitectura sigue un enfoque distribuido basado en Spring Cloud:

```text
CLIENTE EXTERNO
       │
       ▼
API GATEWAY (8080)
       │
 ┌─────┼─────┐
 ▼     ▼     ▼
CUSTOMER   ACCOUNT   OPERATION
 SERVICE    SERVICE    SERVICE
 8081        8082       8083
       │
       ▼
 EUREKA SERVER (8761)

CONFIG SERVER (8888)
AUTH SERVER (9000)
```

Cada microservicio tiene:
- Su propia base de datos.
- Sus propias entidades y lógica de negocio.
- Comunicación mediante HTTP REST usando Feign Client.
- Registro automático en Eureka.

---

# Microservicios del sistema

| Servicio | Responsabilidad | Puerto |
|----------|----------------|---------|
| `customer-service` | Gestión de clientes bancarios | 8081 |
| `account-service` | Gestión de cuentas y movimientos | 8082 |
| `operation-service` | Operaciones financieras | 8083 |
| `eureka-server` | Descubrimiento de servicios | 8761 |
| `config-server` | Configuración centralizada | 8888 |
| `api-gateway` | Enrutamiento y seguridad | 8080 |
| `auth-server` | Emisión y validación JWT | 9000 |

---

# Arquitectura interna de cada microservicio

Cada servicio sigue una arquitectura por capas:

```text
CONTROLLER → SERVICE → REPOSITORY → MODEL
```

| Capa | Responsabilidad |
|------|----------------|
| `controller` | Gestión de endpoints REST |
| `service` | Lógica de negocio |
| `repository` | Acceso a datos con JPA |
| `model` | Entidades del dominio |
| `dto` | Transferencia de datos |
| `client` | Comunicación entre microservicios |
| `exception` | Manejo global de errores |
| `config` | Configuración del servicio |

---

# Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Cloud**
- **Spring Data JPA**
- **Spring Security**
- **JWT / OAuth2**
- **Spring Cloud Gateway**
- **Netflix Eureka**
- **Spring Cloud Config**
- **OpenFeign**
- **Resilience4j**
- **PostgreSQL**
- **Swagger / OpenAPI**
- **JUnit 5**
- **Mockito**
- **WireMock**
- **Maven**

---

# Requisitos

Para ejecutar el proyecto necesitas:

- **JDK 17**
- **Apache Maven 3.8+**
- **PostgreSQL**
- **Git**
- **Postman** (opcional)

---

# Estructura del proyecto

```text
novabank-microservices/
│
├── eureka-server/
├── config-server/
├── api-gateway/
├── auth-server/
├── customer-service/
├── account-service/
├── operation-service/
└── config-repo/
```

---

# Bases de datos

Cada microservicio posee su propia base de datos.

## Crear bases de datos

```sql
CREATE DATABASE novabank_customers;
CREATE DATABASE novabank_accounts;
CREATE DATABASE novabank_operations;
```

---

# Configuración

## Config Server

Las configuraciones se almacenan en un repositorio Git local:

```bash
mkdir ~/novabank-config-repo
cd ~/novabank-config-repo
git init
```

Ejemplo de configuración para `customer-service.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/novabank_customers
    username: postgres
    password: postgres

server:
  port: 8081
```

---

# Eureka Server

Servicio encargado del registro y descubrimiento de microservicios.

## Acceso al dashboard

```text
http://localhost:8761
```

Todos los servicios deben aparecer registrados correctamente.

---

# API Gateway

El Gateway centraliza:
- Enrutamiento.
- Seguridad.
- Validación JWT.
- Balanceo de carga.

## Rutas configuradas

| Ruta | Servicio destino |
|------|------------------|
| `/api/customers/**` | CUSTOMER-SERVICE |
| `/api/accounts/**` | ACCOUNT-SERVICE |
| `/api/operations/**` | OPERATION-SERVICE |
| `/api/auth/**` | AUTH-SERVER |

---

# Autenticación JWT

La autenticación se gestiona mediante `auth-server`.

## Obtener token

```bash
POST http://localhost:8080/api/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "password"
}
```

Respuesta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Usar token

```text
Authorization: Bearer <jwt_token>
```

---

# Comunicación entre microservicios

La comunicación síncrona se realiza mediante **Feign Client**.

Ejemplo:

```java
@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomer(@PathVariable Long id);
}
```

---

# Resiliencia

El sistema implementa tolerancia a fallos mediante:

- **Circuit Breaker**
- **Retry**
- **Fallbacks**

Usando:

```text
Resilience4j
```

Ejemplo de fallback:

```java
@Component
public class CustomerServiceFallback implements CustomerServiceClient {

    @Override
    public CustomerDTO getCustomer(Long id) {
        return new CustomerDTO(id, "Unavailable");
    }
}
```

---

# Ejecución del sistema

## Compilar proyecto

```bash
mvn clean install
```

---

# Orden de arranque

Es importante arrancar los servicios en este orden:

1. `eureka-server`
2. `config-server`
3. `auth-server`
4. `customer-service`
5. `account-service`
6. `operation-service`
7. `api-gateway`

---

# Arrancar servicios

Ejemplo:

```bash
cd eureka-server
mvn spring-boot:run
```

Repetir el proceso para cada microservicio.

---

# Endpoints principales

| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| POST | `/api/auth/login` | Obtener JWT |
| POST | `/api/customers` | Crear cliente |
| GET | `/api/customers` | Listar clientes |
| POST | `/api/accounts` | Crear cuenta |
| GET | `/api/accounts/{id}` | Obtener cuenta |
| GET | `/api/accounts/{id}/movements` | Historial de movimientos |
| POST | `/api/operations/deposit` | Realizar depósito |
| POST | `/api/operations/withdrawal` | Realizar retiro |
| POST | `/api/operations/transfer` | Transferencia bancaria |

---

# Testing

El proyecto incluye:

- Tests unitarios.
- Tests de integración.
- Tests de repositorio.
- Tests de controlador.
- Tests de contrato con WireMock.

## Ejecutar tests

```bash
mvn test
```

---

# Patrones de diseño utilizados

- **Microservices Architecture**
- **API Gateway**
- **Service Discovery**
- **Circuit Breaker**
- **Retry**
- **Fallback**
- **Repository**
- **DTO / Mapper**
- **Singleton**
- **Factory**
- **Dependency Injection**

---

# Swagger / OpenAPI

Cada microservicio expone documentación Swagger.

Ejemplo:

```text
http://localhost:8081/swagger-ui.html
```

---

# Ventajas de la arquitectura

- Escalabilidad independiente.
- Despliegue desacoplado.
- Mejor separación de responsabilidades.
- Mayor tolerancia a fallos.
- Mantenimiento simplificado.
- Servicios independientes por dominio.

---

# Limitaciones actuales

- Las transferencias distribuidas no son completamente atómicas.
- La consistencia entre servicios depende de llamadas HTTP.
- Mayor complejidad operativa frente al monolito.
- Necesidad de observabilidad y monitoreo distribuido.

---

# Futuras mejoras

- Comunicación asíncrona con Kafka.
- Docker Compose.
- Kubernetes.
- Observabilidad con Zipkin y Prometheus.
- Arquitectura reactiva con WebFlux.
- Implementación del patrón SAGA.

# MyTodoList — CRUD Spring Boot + OCI DevOps

Proyecto académico de Spring Boot con CRUD de tareas (ToDoItem), desplegable en local con H2 o en Oracle Cloud Infrastructure.

## Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| JDK         | 17+           |
| Maven       | 3.8+ (o usar `mvnw`) |
| Docker      | 20+ (opcional) |
| Node.js     | 18+ (solo si compilas frontend) |

## Estructura principal

```
MtdrSpring/backend/
├── src/main/java/com/springboot/MyTodoList/
│   ├── controller/   ← REST controllers (ToDoItem, User)
│   ├── dto/          ← Request/Response DTOs
│   ├── model/        ← Entidades JPA (ToDoItem, User)
│   ├── repository/   ← Spring Data repositories
│   ├── service/      ← Lógica de negocio
│   ├── config/       ← Configuración (DB, CORS, DeepSeek)
│   ├── security/     ← Spring Security (permite todo)
│   └── util/         ← Bot de Telegram (opcional)
├── src/main/resources/
│   ├── application.properties          ← Config compartida
│   ├── application-local.properties    ← Perfil H2 local
│   ├── application-oracle.properties   ← Perfil Oracle
│   └── data-local.sql                  ← Datos semilla
├── src/test/                           ← Tests automáticos
├── src/main/frontend/                  ← React frontend
└── pom.xml
```

## Ejecución local (perfil H2)

```bash
cd MtdrSpring/backend

# Compilar y ejecutar (sin frontend)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# O en Windows con mvnw.cmd:
mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

La aplicación arranca en **http://localhost:8080** con base de datos H2 en memoria.

### URLs útiles en modo local

| URL | Descripción |
|-----|-------------|
| http://localhost:8080/api/todos | API REST de tareas |
| http://localhost:8080/swagger-ui.html | Documentación Swagger UI |
| http://localhost:8080/h2-console | Consola H2 (JDBC URL: `jdbc:h2:mem:todolistdb`) |

## Ejecución con Docker

```powershell
cd MtdrSpring/backend

# Compilar JAR
mvnw.cmd clean package -DskipTests -Dmaven.frontend.skip=true

# Construir y ejecutar contenedor
docker build -f DockerfileDev -t agileimage:0.1 .
docker run --name agilecontainer -e SPRING_PROFILES_ACTIVE=local -p 8080:8080 -d agileimage:0.1

# O usar el script PowerShell:
powershell -ExecutionPolicy Bypass -File buildImgContainer.ps1
```

## Endpoints CRUD — ToDoItem

### API REST limpia (`/api/todos`)

| Método | Endpoint | Descripción | Status |
|--------|----------|-------------|--------|
| GET    | `/api/todos` | Listar todas las tareas | 200 |
| GET    | `/api/todos/{id}` | Obtener tarea por ID | 200 / 404 |
| POST   | `/api/todos` | Crear nueva tarea | 201 |
| PUT    | `/api/todos/{id}` | Actualizar tarea | 200 / 404 |
| DELETE | `/api/todos/{id}` | Eliminar tarea | 204 / 404 |

### Ejemplos de request/response

**Crear tarea:**
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"description": "Mi primera tarea", "done": false}'
```
Respuesta (201):
```json
{
  "id": 1,
  "description": "Mi primera tarea",
  "creationTs": "2026-03-26T10:30:00-06:00",
  "done": false
}
```

**Listar tareas:**
```bash
curl http://localhost:8080/api/todos
```

**Obtener por ID:**
```bash
curl http://localhost:8080/api/todos/1
```

**Actualizar tarea:**
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"description": "Tarea actualizada", "done": true}'
```

**Eliminar tarea:**
```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

## Endpoints CRUD — User

| Método | Endpoint | Descripción | Status |
|--------|----------|-------------|--------|
| GET    | `/api/users` | Listar usuarios | 200 |
| GET    | `/api/users/{id}` | Obtener usuario por ID | 200 / 404 |
| POST   | `/api/users` | Crear usuario | 201 |
| PUT    | `/api/users/{id}` | Actualizar usuario | 200 / 404 |
| DELETE | `/api/users/{id}` | Eliminar usuario | 204 / 404 |

## Tests

```bash
cd MtdrSpring/backend
./mvnw test -Dspring.profiles.active=local
```

Los tests validan: listar, crear, obtener por ID, actualizar, eliminar, 404 por ID inexistente, y validación de campos.

## Configuración Oracle (perfil oracle)

Para conectar a Oracle Autonomous DB:
1. Coloca tu wallet en `MtdrSpring/backend/wallet/`
2. Configura variables de entorno:
   ```bash
   export SPRING_PROFILES_ACTIVE=oracle
   export db_user=TU_USUARIO
   export dbpassword=TU_PASSWORD
   export db_url=jdbc:oracle:thin:@tudb_tp?TNS_ADMIN=/ruta/wallet
   ```
3. Ejecuta: `./mvnw spring-boot:run`

## Servicios opcionales

- **Telegram Bot**: Se activa con `TELEGRAM_BOT_ENABLED=true` y token válido.
- **DeepSeek LLM**: Se activa configurando `DEEPSEEK_API_KEY` con una clave real.
- Ambos están **desactivados por defecto** y no bloquean el arranque.

## Liga del repositorio

**https://github.com/jorgecarvel/oci_devops_project**

---

> Proyecto académico — Spring Boot + Docker + OCI DevOps

⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub
# Evidencia del CRUD — MyTodoList

## Comandos para arrancar el proyecto

### Opción 1: Ejecución directa (requiere JDK 17+)

```bash
cd MtdrSpring/backend
mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

### Opción 2: Con Docker

```powershell
cd MtdrSpring/backend
mvnw.cmd clean package -DskipTests "-Dmaven.frontend.skip=true"
docker build -f DockerfileDev -t agileimage:0.1 .
docker run --name agilecontainer -e SPRING_PROFILES_ACTIVE=local -p 8080:8080 -d agileimage:0.1
```

### Verificar que arrancó

```bash
curl http://localhost:8080/api/todos
```

Debe devolver un array JSON con los datos semilla.

---

## Comandos curl para demostrar el CRUD

### 1. LISTAR todas las tareas (READ)

```bash
curl -s http://localhost:8080/api/todos | python -m json.tool
```

**Evidencia esperada:** Lista JSON con 5 tareas semilla.

### 2. OBTENER tarea por ID (READ)

```bash
curl -s http://localhost:8080/api/todos/1 | python -m json.tool
```

**Evidencia esperada:** JSON con la tarea de id=1.

### 3. CREAR nueva tarea (CREATE)

```bash
curl -s -X POST http://localhost:8080/api/todos -H "Content-Type: application/json" -d "{\"description\": \"Tarea creada para evidencia\", \"done\": false}" | python -m json.tool
```

**Evidencia esperada:** Respuesta 201 con JSON de la nueva tarea y su ID generado.

### 4. ACTUALIZAR tarea (UPDATE)

```bash
curl -s -X PUT http://localhost:8080/api/todos/1 -H "Content-Type: application/json" -d "{\"description\": \"Tarea actualizada para evidencia\", \"done\": true}" | python -m json.tool
```

**Evidencia esperada:** Respuesta 200 con la tarea actualizada, `done: true`.

### 5. ELIMINAR tarea (DELETE)

```bash
curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8080/api/todos/1
```

**Evidencia esperada:** Status code `204` (No Content).

### 6. VERIFICAR eliminación

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/todos/1
```

**Evidencia esperada:** Status code `404`.

---

## Comandos PowerShell equivalentes (Windows)

```powershell
# LISTAR
Invoke-RestMethod -Uri http://localhost:8080/api/todos | ConvertTo-Json

# CREAR
$body = '{"description": "Tarea de evidencia", "done": false}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/todos -ContentType "application/json" -Body $body | ConvertTo-Json

# OBTENER
Invoke-RestMethod -Uri http://localhost:8080/api/todos/1 | ConvertTo-Json

# ACTUALIZAR
$body = '{"description": "Tarea modificada", "done": true}'
Invoke-RestMethod -Method Put -Uri http://localhost:8080/api/todos/1 -ContentType "application/json" -Body $body | ConvertTo-Json

# ELIMINAR
Invoke-WebRequest -Method Delete -Uri http://localhost:8080/api/todos/1
```

---

## Swagger UI

Abrir en el navegador: **http://localhost:8080/swagger-ui.html**

Desde ahí puedes probar todos los endpoints visualmente.

---

## Tests automáticos

```bash
cd MtdrSpring/backend
mvnw.cmd test "-Dspring.profiles.active=local"
```

**Evidencia esperada:** 10 tests pasando:
- Listar vacío
- Crear tarea
- Listar después de crear
- Obtener por ID
- 404 por ID inexistente
- Actualizar tarea
- 404 al actualizar inexistente
- Eliminar tarea
- 404 al eliminar inexistente
- Validación de campo vacío (400)

---

## Checklist de screenshots para la entrega

- [ ] Terminal: proyecto arrancando con perfil local
- [ ] Navegador: Swagger UI mostrando los endpoints
- [ ] curl/Postman: GET /api/todos (listar)
- [ ] curl/Postman: POST /api/todos (crear) — respuesta 201
- [ ] curl/Postman: GET /api/todos/{id} (obtener el creado)
- [ ] curl/Postman: PUT /api/todos/{id} (actualizar) — respuesta 200
- [ ] curl/Postman: DELETE /api/todos/{id} (eliminar) — respuesta 204
- [ ] curl/Postman: GET /api/todos/{id} después de eliminar — respuesta 404
- [ ] Terminal: tests automáticos pasando (mvnw test)
- [ ] Navegador: frontend React mostrando lista de tareas (opcional)
- [ ] Navegador: H2 Console mostrando tabla TODOITEM (opcional)

---

## Liga del repositorio

**https://github.com/jorgecarvel/oci_devops_project**

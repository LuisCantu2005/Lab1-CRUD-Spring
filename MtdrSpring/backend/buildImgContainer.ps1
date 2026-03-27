# Detener y limpiar contenedor previo
docker stop agilecontainer 2>$null
docker rm -f agilecontainer 2>$null
docker rmi agileimage 2>$null

# Compilar proyecto (saltando frontend para builds rápidos locales)
mvn clean package -DskipTests -pl . -P !frontend

# Construir imagen Docker
docker build -f DockerfileDev --platform linux/amd64 -t agileimage:0.1 .

# Ejecutar con perfil local (H2) por defecto
# Para Oracle, agregar: -e SPRING_PROFILES_ACTIVE=oracle -e db_user=... -e dbpassword=... -e db_url=...
docker run --name agilecontainer -e SPRING_PROFILES_ACTIVE=local -p 8080:8080 -d agileimage:0.1

Write-Host ""
Write-Host "Contenedor iniciado. Accede a:"
Write-Host "  API:     http://localhost:8080/api/todos"
Write-Host "  Swagger: http://localhost:8080/swagger-ui.html"
Write-Host "  H2:      http://localhost:8080/h2-console"
Write-Host ""
Write-Host "Para ver logs: docker logs -f agilecontainer"
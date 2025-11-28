#!/bin/bash

# 📋 Script de Verificación - Configuración Docker Ludico Backend
# Uso: bash VERIFY_DOCKER_CONFIG.sh

echo "======================================"
echo "🔍 Verificando Configuración Docker"
echo "======================================"
echo ""

# Variables de color
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Contador de errores
ERRORS=0

# Función para validar archivo
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅${NC} Encontrado: $1"
    else
        echo -e "${RED}❌${NC} FALTA: $1"
        ERRORS=$((ERRORS + 1))
    fi
}

# Función para validar contenido
check_content() {
    if grep -q "$2" "$1" 2>/dev/null; then
        echo -e "${GREEN}✅${NC} $1 contiene: '$2'"
    else
        echo -e "${RED}❌${NC} $1 NO contiene: '$2'"
        ERRORS=$((ERRORS + 1))
    fi
}

echo "📁 Verificando archivos de configuración..."
echo ""

# Archivos DOCKER de configuración
echo "🔹 Configuraciones con perfil 'docker':"
check_file "microservice-config/src/main/resources/configurations/msvc-user-docker.yml"
check_file "microservice-config/src/main/resources/configurations/microservice-event-docker.yml"
check_file "microservice-config/src/main/resources/configurations/msvc-gateway-docker.yml"
check_file "microservice-config/src/main/resources/configurations/msvc-eureka-docker.yml"
check_file "microservice-config/src/main/resources/configurations/microservice-swagger-central-docker.yml"
check_file "microservice-config/src/main/resources/application-docker.yml"

echo ""
echo "🔹 Documentación:"
check_file "DOCKER_LOCAL_SETUP.md"
check_file "CAMBIOS_DOCKER_REALIZADOS.md"

echo ""
echo "🔹 Configuraciones actualizadas:"
check_content "microservice-gateway/src/main/resources/application.yml" "profiles"
check_content "microservice-eureka/src/main/resources/application.yml" "profiles"

echo ""
echo "📋 Verificando contenido de configuraciones..."
echo ""

# Verificar contenido de msvc-user-docker.yml
echo "🔹 msvc-user-docker.yml:"
check_content "microservice-config/src/main/resources/configurations/msvc-user-docker.yml" "postgresql"
check_content "microservice-config/src/main/resources/configurations/msvc-user-docker.yml" "postgres:5432/ludico_db"
check_content "microservice-config/src/main/resources/configurations/msvc-user-docker.yml" "eureka-server"

# Verificar contenido de microservice-event-docker.yml
echo ""
echo "🔹 microservice-event-docker.yml:"
check_content "microservice-config/src/main/resources/configurations/microservice-event-docker.yml" "postgresql"
check_content "microservice-config/src/main/resources/configurations/microservice-event-docker.yml" "/app/uploads/events"
check_content "microservice-config/src/main/resources/configurations/microservice-event-docker.yml" "eureka-server"

# Verificar contenido de msvc-gateway-docker.yml
echo ""
echo "🔹 msvc-gateway-docker.yml:"
check_content "microservice-config/src/main/resources/configurations/msvc-gateway-docker.yml" "discovery"
check_content "microservice-config/src/main/resources/configurations/msvc-gateway-docker.yml" "lb://msvc-user"
check_content "microservice-config/src/main/resources/configurations/msvc-gateway-docker.yml" "lb://microservice-event"

# Verificar docker-compose.yml
echo ""
echo "🔹 docker-compose.yml:"
check_content "docker-compose.yml" "event_uploads"
check_content "docker-compose.yml" "service_healthy"
check_content "docker-compose.yml" "healthcheck"
check_content "docker-compose.yml" "SPRING_PROFILES_ACTIVE: docker"

echo ""
echo "======================================"
echo "📊 RESULTADO FINAL"
echo "======================================"
echo ""

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✅ TODAS LAS VERIFICACIONES PASARON${NC}"
    echo ""
    echo "🚀 Estás listo para ejecutar:"
    echo "   docker-compose build"
    echo "   docker-compose up -d"
else
    echo -e "${RED}❌ ENCONTRADOS $ERRORS ERRORES${NC}"
    echo ""
    echo "Por favor, revisa los puntos marcados en rojo arriba"
fi

echo ""

@echo off
REM 📋 Script de Utilidades Docker - Ludico Backend
REM Para Windows PowerShell
REM Guarda como: docker-commands.bat o úsalo en PowerShell

echo.
echo ======================================
echo Docker Ludico Backend - Utilidades
echo ======================================
echo.

REM Verifica argumentos
if "%1"=="" goto menu
if "%1"=="build" goto build
if "%1"=="up" goto up
if "%1"=="down" goto down
if "%1"=="logs" goto logs
if "%1"=="status" goto status
if "%1"=="restart" goto restart
if "%1"=="clean" goto clean
if "%1"=="test-eureka" goto test_eureka
if "%1"=="test-config" goto test_config
if "%1"=="test-gateway" goto test_gateway

goto menu

:menu
echo Comandos disponibles:
echo.
echo   docker-commands build            - Construir imágenes Docker
echo   docker-commands up               - Iniciar servicios
echo   docker-commands down             - Detener servicios
echo   docker-commands logs             - Ver logs en vivo
echo   docker-commands status           - Estado de los contenedores
echo   docker-commands restart          - Reiniciar servicios
echo   docker-commands clean            - Limpiar volúmenes y contenedores
echo   docker-commands test-eureka      - Probar Eureka (8761)
echo   docker-commands test-config      - Probar Config Server (8888)
echo   docker-commands test-gateway     - Probar Gateway (8080)
echo.
goto end

:build
echo 🐳 Construyendo imágenes Docker...
docker-compose build
echo ✅ Build completado
goto end

:up
echo 🚀 Iniciando servicios...
docker-compose up -d
echo ✅ Servicios iniciados
echo.
echo Esperando ~30 segundos para que los servicios estén listos...
timeout /t 30 /nobreak
echo.
echo 📋 Estado de los servicios:
docker ps --format "table {{.Names}}\t{{.Status}}"
goto end

:down
echo ⛔ Deteniendo servicios...
docker-compose down
echo ✅ Servicios detenidos
goto end

:logs
echo 📜 Mostrando logs en vivo (Ctrl+C para salir)...
docker-compose logs -f
goto end

:status
echo 📊 Estado actual de los contenedores:
echo.
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo.
goto end

:restart
echo 🔄 Reiniciando servicios...
docker-compose down
timeout /t 5 /nobreak
docker-compose up -d
echo ✅ Servicios reiniciados
goto end

:clean
echo 🧹 Limpiando volúmenes y contenedores...
echo.
echo ⚠️  Esto eliminará todos los datos persistentes!
pause
docker-compose down -v
echo ✅ Limpieza completada
goto end

:test_eureka
echo 🔍 Probando Eureka Server en localhost:8761...
echo.
timeout /t 2 /nobreak
powershell -Command "try { (Invoke-WebRequest http://localhost:8761).StatusCode; Write-Host '✅ Eureka está disponible' } catch { Write-Host '❌ Eureka no responde' }"
goto end

:test_config
echo 🔍 Probando Config Server en localhost:8888...
echo.
timeout /t 2 /nobreak
powershell -Command "try { (Invoke-WebRequest http://localhost:8888).StatusCode; Write-Host '✅ Config Server está disponible' } catch { Write-Host '❌ Config Server no responde' }"
goto end

:test_gateway
echo 🔍 Probando Gateway en localhost:8080...
echo.
timeout /t 2 /nobreak
powershell -Command "try { (Invoke-WebRequest http://localhost:8080/actuator/health).StatusCode; Write-Host '✅ Gateway está disponible' } catch { Write-Host '❌ Gateway no responde' }"
goto end

:end
echo.

# 📋 Script de Utilidades Docker - Ludico Backend
# Para Windows PowerShell
# Uso: .\docker-commands.ps1 build
# o:   .\docker-commands.ps1 up

param(
    [string]$Command = ""
)

function Show-Menu {
    Write-Host ""
    Write-Host "======================================"
    Write-Host "Docker Ludico Backend - Utilidades" -ForegroundColor Cyan
    Write-Host "======================================"
    Write-Host ""
    Write-Host "Comandos disponibles:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "  .\docker-commands.ps1 build            - Construir imágenes Docker"
    Write-Host "  .\docker-commands.ps1 up               - Iniciar servicios"
    Write-Host "  .\docker-commands.ps1 down             - Detener servicios"
    Write-Host "  .\docker-commands.ps1 logs             - Ver logs en vivo"
    Write-Host "  .\docker-commands.ps1 status           - Estado de los contenedores"
    Write-Host "  .\docker-commands.ps1 restart          - Reiniciar servicios"
    Write-Host "  .\docker-commands.ps1 clean            - Limpiar volúmenes y contenedores"
    Write-Host "  .\docker-commands.ps1 test-eureka      - Probar Eureka (8761)"
    Write-Host "  .\docker-commands.ps1 test-config      - Probar Config Server (8888)"
    Write-Host "  .\docker-commands.ps1 test-gateway     - Probar Gateway (8080)"
    Write-Host "  .\docker-commands.ps1 health-check     - Verificar salud de todos los servicios"
    Write-Host ""
}

function Build {
    Write-Host "🐳 Construyendo imágenes Docker..." -ForegroundColor Green
    docker-compose build
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Build completado" -ForegroundColor Green
    } else {
        Write-Host "❌ Error en el build" -ForegroundColor Red
    }
}

function Up {
    Write-Host "🚀 Iniciando servicios..." -ForegroundColor Green
    docker-compose up -d
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Servicios iniciados" -ForegroundColor Green
        Write-Host ""
        Write-Host "⏳ Esperando ~30 segundos para que los servicios estén listos..."
        Start-Sleep -Seconds 30
        Write-Host ""
        Write-Host "📋 Estado de los servicios:" -ForegroundColor Cyan
        docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    } else {
        Write-Host "❌ Error al iniciar servicios" -ForegroundColor Red
    }
}

function Down {
    Write-Host "⛔ Deteniendo servicios..." -ForegroundColor Yellow
    docker-compose down
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Servicios detenidos" -ForegroundColor Green
    } else {
        Write-Host "❌ Error al detener servicios" -ForegroundColor Red
    }
}

function Logs {
    Write-Host "📜 Mostrando logs en vivo (Ctrl+C para salir)..." -ForegroundColor Green
    docker-compose logs -f
}

function Status {
    Write-Host "📊 Estado actual de los contenedores:" -ForegroundColor Cyan
    Write-Host ""
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    Write-Host ""
}

function Restart {
    Write-Host "🔄 Reiniciando servicios..." -ForegroundColor Yellow
    docker-compose down
    Start-Sleep -Seconds 3
    docker-compose up -d
    Write-Host "✅ Servicios reiniciados" -ForegroundColor Green
    Start-Sleep -Seconds 10
    Status
}

function Clean {
    Write-Host "🧹 Limpiando volúmenes y contenedores..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "⚠️  Esto eliminará todos los datos persistentes!" -ForegroundColor Red
    $confirmation = Read-Host "¿Estás seguro? (s/n)"
    
    if ($confirmation -eq 's' -or $confirmation -eq 'S') {
        docker-compose down -v
        Write-Host "✅ Limpieza completada" -ForegroundColor Green
    } else {
        Write-Host "Operación cancelada" -ForegroundColor Yellow
    }
}

function Test-Eureka {
    Write-Host "🔍 Probando Eureka Server en localhost:8761..." -ForegroundColor Green
    Write-Host ""
    
    try {
        $response = Invoke-WebRequest http://localhost:8761 -TimeoutSec 3
        Write-Host "✅ Eureka está disponible (Status: $($response.StatusCode))" -ForegroundColor Green
        Write-Host "📍 Acceder a: http://localhost:8761" -ForegroundColor Cyan
    } catch {
        Write-Host "❌ Eureka no responde" -ForegroundColor Red
        Write-Host "💡 Espera unos segundos más o verifica los logs" -ForegroundColor Yellow
    }
}

function Test-Config {
    Write-Host "🔍 Probando Config Server en localhost:8888..." -ForegroundColor Green
    Write-Host ""
    
    try {
        $response = Invoke-WebRequest http://localhost:8888 -TimeoutSec 3
        Write-Host "✅ Config Server está disponible (Status: $($response.StatusCode))" -ForegroundColor Green
        Write-Host "📍 Acceder a: http://localhost:8888" -ForegroundColor Cyan
    } catch {
        Write-Host "❌ Config Server no responde" -ForegroundColor Red
        Write-Host "💡 Espera unos segundos más o verifica los logs" -ForegroundColor Yellow
    }
}

function Test-Gateway {
    Write-Host "🔍 Probando Gateway en localhost:8080..." -ForegroundColor Green
    Write-Host ""
    
    try {
        $response = Invoke-WebRequest http://localhost:8080/actuator/health -TimeoutSec 3
        Write-Host "✅ Gateway está disponible (Status: $($response.StatusCode))" -ForegroundColor Green
        Write-Host "📍 Acceder a: http://localhost:8080" -ForegroundColor Cyan
    } catch {
        Write-Host "❌ Gateway no responde" -ForegroundColor Red
        Write-Host "💡 Espera unos segundos más o verifica los logs" -ForegroundColor Yellow
    }
}

function Health-Check {
    Write-Host "🏥 Verificando salud de todos los servicios..." -ForegroundColor Cyan
    Write-Host ""
    
    $services = @(
        @{Name="Config Server"; Url="http://localhost:8888"; Port=8888},
        @{Name="Eureka"; Url="http://localhost:8761"; Port=8761},
        @{Name="User Service"; Url="http://localhost:8082/actuator/health"; Port=8082},
        @{Name="Event Service"; Url="http://localhost:8083/actuator/health"; Port=8083},
        @{Name="Swagger"; Url="http://localhost:8085"; Port=8085},
        @{Name="Gateway"; Url="http://localhost:8080/actuator/health"; Port=8080},
        @{Name="PostgreSQL"; Url="localhost:5432"; Port=5432}
    )
    
    foreach ($service in $services) {
        Write-Host -NoNewline "$($service.Name)... "
        
        try {
            if ($service.Name -eq "PostgreSQL") {
                # Para PostgreSQL, simplemente verificamos que el puerto esté listening
                if (Test-NetConnection -ComputerName localhost -Port $service.Port -WarningAction SilentlyContinue).TcpTestSucceeded) {
                    Write-Host "✅" -ForegroundColor Green
                } else {
                    Write-Host "❌" -ForegroundColor Red
                }
            } else {
                $response = Invoke-WebRequest $service.Url -TimeoutSec 2 -WarningAction SilentlyContinue
                Write-Host "✅" -ForegroundColor Green
            }
        } catch {
            Write-Host "❌" -ForegroundColor Red
        }
    }
    
    Write-Host ""
    Write-Host "Servicios expuestos:" -ForegroundColor Yellow
    Write-Host "  - Eureka Dashboard:    http://localhost:8761" -ForegroundColor Cyan
    Write-Host "  - Config Server:       http://localhost:8888" -ForegroundColor Cyan
    Write-Host "  - API Gateway:         http://localhost:8080" -ForegroundColor Cyan
    Write-Host "  - User Service:        http://localhost:8082" -ForegroundColor Cyan
    Write-Host "  - Event Service:       http://localhost:8083" -ForegroundColor Cyan
    Write-Host "  - Swagger UI:          http://localhost:8085" -ForegroundColor Cyan
    Write-Host ""
}

# Main script execution
switch ($Command.ToLower()) {
    "build" { Build }
    "up" { Up }
    "down" { Down }
    "logs" { Logs }
    "status" { Status }
    "restart" { Restart }
    "clean" { Clean }
    "test-eureka" { Test-Eureka }
    "test-config" { Test-Config }
    "test-gateway" { Test-Gateway }
    "health-check" { Health-Check }
    default { Show-Menu }
}

Write-Host ""

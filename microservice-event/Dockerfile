# 1. ETAPA DE BUILD
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar POM Padre y el módulo actual para compilar
# POR QUÉ: Necesitamos la dependencia al POM Padre para que Maven funcione.
COPY pom.xml ./
COPY microservice-config/ microservice-config/
COPY microservice-eureka/ microservice-eureka/
COPY microservice-gateway/ microservice-gateway/
COPY microservice-event/ microservice-event/
COPY microservice-swagger-central/ microservice-swagger-central/
COPY microservice-user/ microservice-user/

# CAMBIO CLAVE: Comando de Maven con ajustes de red y memoria
# POR QUÉ: Aumentamos la memoria (-Xmx1g) y forzamos protocolos TLS robustos para evitar el 'handshake failure'.
# También usamos -B para el modo batch (no interactivo).
RUN export MAVEN_OPTS="-Xmx1g" && \
    mvn clean package \
    -Dhttps.protocols=TLSv1.2,TLSv1.3 \
    -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN \
    -B \
    -U \
    -pl microservice-event \
    -am \
    -DskipTests

# 2. ETAPA DE RUNTIME
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia el JAR compilado
COPY --from=builder /app/microservice-event/target/*.jar app.jar

# Puerto interno del contenedor (Spring Boot por defecto)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java","-jar","/app/app.jar"]
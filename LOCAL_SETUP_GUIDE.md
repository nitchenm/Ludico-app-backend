# Local Development Setup Guide (MySQL & No Docker)

This guide provides step-by-step instructions to run the Ludico microservices project on your local machine, using a **MySQL** database and without relying on Docker.

---

## 1. Important: Required Code Modifications

Before you can run the application, you must manually modify the project to switch from PostgreSQL to MySQL.

### A. Update `pom.xml` Dependencies

In each of the following microservices, you need to replace the PostgreSQL driver dependency with the one for MySQL.

**Affected files:**

*   `microservice-user/pom.xml`
*   `microservice-event/pom.xml`

**Change to make:**

Find and **remove** the following dependency block:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

And **add** the following MySQL dependency block in its place:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

### B. Update Configuration Files

The centralized configuration files must be updated to point to a MySQL database instead of PostgreSQL.

**Affected files:**

*   `microservice-config/src/main/resources/configurations/msvc-user.yml`
*   `microservice-config/src/main/resources/configurations/microservice-event.yml`

**Change to make:**

In both files, find the `spring.datasource` and `spring.jpa` sections.

**Replace** this entire block:

```yaml
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/ludico_db
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: create-drop
    sql:
      init:
        mode: always
    show-sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

**With this new block** configured for MySQL:

```yaml
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ludico_db?createDatabaseIfNotExist=true&useSSL=false
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database: mysql
    database-platform: org.hibernate.dialect.MySQLDialect
```

---

## 2. Database Setup

After making the code changes, set up your MySQL database.

1.  **Start Laragon** and ensure your **MySQL** server is running.
2.  **Create a new database** with the following details:
    *   **Database Name:** `ludico_db`
3.  **Create a database user** with the following credentials and grant it privileges to the `ludico_db` database:
    *   **Username:** `user`
    *   **Password:** `password`
    *   _Note: The `url` in the configuration is set to create the database if it doesn't exist, but it's good practice to create it yourself._

---

## 3. Running the Microservices

The microservices must be started in a specific order. Open a new terminal for each service, navigate to its root folder, and run it using the Maven Spring Boot plugin.

**Start Order:**

1.  **Run `microservice-config`**
    ```bash
    cd microservice-config
    mvn spring-boot:run
    ```

2.  **Run `microservice-eureka`**
    ```bash
    cd microservice-eureka
    mvn spring-boot:run
    ```
    (Verify at `http://localhost:8761/`)

3.  **Run Backend Microservices** (in any order)
    ```bash
    cd microservice-user
    mvn spring-boot:run
    ```
    ```bash
    cd microservice-event
    mvn spring-boot:run
    ```
    ```bash
    cd microservice-swagger-central
    mvn spring-boot:run
    ```

4.  **Run `microservice-gateway`**
    ```bash
    cd microservice-gateway
    mvn spring-boot:run
    ```

---

## 4. Optional: File Cleanup

Since you are no longer using Docker, you can safely delete the following files from the project to keep it clean:

*   `docker-compose.yml`
*   `docker-commands.bat`
*   `docker-commands.ps1`
*   `VERIFY_DOCKER_CONFIG.sh`
*   All files named `Dockerfile` in the root of each microservice directory (e.g., `microservice-user/Dockerfile`).
*   All configuration files ending in `-docker.yml` inside `microservice-config/src/main/resources/configurations/`.
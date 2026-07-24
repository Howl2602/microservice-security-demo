# Validation Report

## Static checks completed

- Parsed all Maven `pom.xml` files as valid XML.
- Parsed all YAML/YML files, including `docker-compose.yml`.
- Parsed the Postman collection as valid JSON.
- Verified Java package declarations match their source directories.
- Verified basic brace balance for Java source files.
- Verified frontend HTML contains all expected views and forms.
- Verified the inline frontend JavaScript passes `node --check`.
- Verified frontend default Gateway address is `http://localhost:8085`.
- Verified Docker Compose contains Eureka, Gateway, three services, three MySQL databases, RabbitMQ and frontend.
- Verified all Spring services use Spring Boot 3.2.5 and Spring Cloud 2023.0.0.

## Runtime limitation of this review environment

A complete Maven build and Docker Compose launch could not be executed in the review sandbox because Docker is unavailable and the sandbox cannot download Maven dependencies. Run the following on the Windows machine with Docker Desktop:

```powershell
docker compose up --build
```

Then run:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke-test.ps1
```

If a runtime error remains, save:

```powershell
docker compose ps
docker compose logs --no-color > microshield-logs.txt
```

and use the log file for the next debugging pass.

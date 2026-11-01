# Backend Development and Startup Guide

This guide describes the current backend layout and a safe local startup sequence. The current root `docker-compose.yml` starts **infrastructure only** (PostgreSQL, Redis, ZooKeeper, Kafka, and Kafka UI); the 12 Spring Boot services are started separately with Maven.

## Prerequisites

- Java 21 (verify with `java -version`)
- Maven 3.9+ (verify with `mvn -version`)
- Docker Engine/Desktop with Compose v2 (verify with `docker compose version`)
- Git and Postman (optional for API testing)

## 1. Configure local environment

From the backend repository root:

```bash
cp .env.example .env
```

Review `.env` and set a strong, random `JWT_SECRET` before running identity-service. Do not commit `.env`. The values in `.env.example` are development placeholders only.

The Compose stack reads variables from `.env`. Spring Boot processes its own environment variables separately, so when starting a service directly on the host, its default database host is `localhost`. The database password default is `local_dev_only_change_me`; keep the Compose and service values aligned if you override it.

## 2. Start infrastructure

```bash
docker compose config
docker compose up -d
docker compose ps
```

Wait until PostgreSQL is healthy before starting Spring services. The first PostgreSQL startup runs `infrastructure/postgres/init-databases.sql`, which creates a separate database for each service. Initialization scripts run only when the PostgreSQL data volume is first initialized.

Local endpoints:

| Component | Endpoint |
| --- | --- |
| PostgreSQL | `localhost:5432` |
| Redis | `localhost:6379` |
| Kafka (host listener) | `localhost:9092` |
| Kafka UI | `http://localhost:8092` |

If you previously initialized PostgreSQL with different credentials, changing `.env` does not change the existing database user's password. Update that user in PostgreSQL or reset the local volume only if its data can safely be discarded.

## 3. Start Spring Boot services

Each service is a standalone Maven project; there is currently no root Maven aggregator. Open a separate terminal for each process. Run commands from the backend root:

```bash
mvn -f identity-service/pom.xml spring-boot:run
mvn -f tenant-service/pom.xml spring-boot:run
mvn -f catalog-service/pom.xml spring-boot:run
mvn -f seller-service/pom.xml spring-boot:run
mvn -f inventory-service/pom.xml spring-boot:run
mvn -f cart-service/pom.xml spring-boot:run
mvn -f order-service/pom.xml spring-boot:run
mvn -f payment-service/pom.xml spring-boot:run
mvn -f consultation-service/pom.xml spring-boot:run
mvn -f notification-service/pom.xml spring-boot:run
mvn -f admin-service/pom.xml spring-boot:run
mvn -f api-gateway/pom.xml spring-boot:run
```

Start infrastructure first, then the domain services, and start the API Gateway last. If a service fails, check its startup logs for database credentials, Flyway migration, port conflicts, and Kafka connectivity.

## 4. Verify service health

Once a service is running, try its actuator health endpoint directly, where actuator is configured:

- Gateway: `http://localhost:8080/actuator/health`
- Identity: `http://localhost:8081/actuator/health`
- Tenant: `http://localhost:8082/actuator/health`
- Catalog: `http://localhost:8083/actuator/health`

The gateway is the frontend's main API entry point at `http://localhost:8080`. Example storefront request:

```http
GET http://localhost:8080/api/v1/storefront/config?tenantCode=AURA_AYURVEDA
```

A health endpoint returning UP does not by itself prove the end-to-end business flow works; use the Postman collection for integration checks.

## 5. Run tests

Run each service's tests independently from the repository root. Example:

```bash
mvn -f catalog-service/pom.xml test
mvn -f api-gateway/pom.xml test
```

To build every service, run `mvn -f <service-directory>/pom.xml verify` for each directory. The current repository does not define a root Maven reactor build.

## 6. Postman

Import `postman/AuraMarket-Backend.postman_collection.json`. If the environment file exists in your checkout, import `postman/AuraMarket-Local.postman_environment.json` as well and select it. Run health checks first, then authentication and tenant/catalog flows, then protected/admin flows.

The collection is a test harness, not proof that all endpoints are implemented or that every authorization rule is production-ready. Confirm each request against the actual controller and response before treating a test as passing.

## 7. Current limitations to resolve before production

- The root Compose file currently starts infrastructure, not the Spring Boot service containers.
- Service-to-service Kafka event contracts and consumer flows need end-to-end verification.
- Authentication and role/tenant authorization must be reviewed for every admin and seller endpoint.
- Payment providers, email/SMS gateways, OAuth credentials, and production secrets require real configuration.
- Run the full Maven test/build suite and Postman collection against a running stack before release.

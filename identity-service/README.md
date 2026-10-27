# Identity Service — AuraMarket Microservices Backend

## Overview

The **Identity Service** (`identity-service`, Port `8081`) serves as the security and authentication foundation for the AuraMarket microservices platform. It handles user registration, authentication, JWT issuing & verification, refresh token rotation, password management, role authorization, and multi-tenant security context.

---

## Technical Specifications

- **Framework**: Spring Boot 3.3.3 / Java 21
- **Security**: Spring Security (Stateless, `SecurityFilterChain`, BCrypt password hashing)
- **Authentication**: JWT Asymmetric/HMAC Tokens & Refresh Token Rotation
- **Database**: PostgreSQL (`auramarket_identity`)
- **Schema Management**: Flyway (`V1__create_identity_schema.sql`)
- **Port**: `8081`

---

## Environment Variables

| Variable | Default Value | Description |
|---|---|---|
| `IDENTITY_DB_URL` | `jdbc:postgresql://localhost:5432/auramarket_identity` | PostgreSQL Database Connection URL |
| `IDENTITY_DB_USERNAME` | `auramarket_user` | Database Username |
| `IDENTITY_DB_PASSWORD` | `auramarket_secure_password_2026` | Database Password |
| `JWT_SECRET` | `aura_market_super_secret_jwt_key_32bytes_min_length_2026` | HMAC/RSA Signing Key Secret |
| `JWT_ISSUER` | `auramarket-identity` | JWT Issuer Claim |
| `JWT_AUDIENCE` | `auramarket-api` | JWT Audience Claim |
| `JWT_ACCESS_TOKEN_EXPIRATION` | `900` | Access Token TTL (15 minutes = 900s) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | `604800` | Refresh Token TTL (7 days = 604800s) |

---

## Development Seed Users

Seeded under tenant `AURA_AYURVEDA` with default password `Password@123`:

- **Customer**: `customer@auramarket.local` (Role: `CUSTOMER`)
- **Seller**: `seller@auramarket.local` (Role: `SELLER`)
- **Doctor**: `doctor@auramarket.local` (Role: `DOCTOR`)
- **Admin**: `admin@auramarket.local` (Role: `ADMIN`)

---

## API Endpoints Summary

- `POST /api/v1/auth/register` — Register new user
- `POST /api/v1/auth/login` — Authenticate and obtain tokens
- `POST /api/v1/auth/refresh` — Rotate refresh token and get new access token
- `POST /api/v1/auth/logout` — Revoke refresh token
- `GET /api/v1/auth/me` — Get current user profile (JWT protected)
- `POST /api/v1/auth/change-password` — Change password & revoke tokens (JWT protected)
- `GET /actuator/health` — Health check status

---

## Build & Run

```bash
# Compile and test
mvn clean verify

# Run locally
mvn spring-boot:run

# Build Docker image
docker build -t auramarket/identity-service:1.0.0 .
```

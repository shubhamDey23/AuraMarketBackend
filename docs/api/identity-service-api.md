# Identity Service API Reference & Curl Examples

The **Identity Service** provides production-grade user registration, login, JWT token issuance, refresh token rotation, logout, password modification, and identity details (`/me`).

---

## Base Endpoints

- **Direct Service URL**: `http://localhost:8081`
- **API Gateway Entry Point**: `http://localhost:8080/api/v1/auth`

---

## 1. Register Customer Account

- **Endpoint**: `POST /api/v1/auth/register`
- **Access**: Public
- **HTTP Status**: `201 Created`

### Curl Command
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "tenantCode": "AURA_AYURVEDA",
    "email": "customer@example.com",
    "password": "Password@123",
    "firstName": "Ayush",
    "lastName": "Sharma",
    "phone": "9876543210"
  }'
```

### Sample Response (`201 Created`)
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": "e4a8f9c1-4b2a-43d9-9f7a-8b1c2d3e4f5a",
    "email": "customer@example.com",
    "firstName": "Ayush",
    "lastName": "Sharma",
    "phone": "9876543210",
    "tenantId": "AURA_AYURVEDA",
    "roles": ["CUSTOMER"],
    "status": "ACTIVE"
  },
  "timestamp": "2026-09-25T20:09:00Z"
}
```

---

## 2. User Login

- **Endpoint**: `POST /api/v1/auth/login`
- **Access**: Public
- **HTTP Status**: `200 OK`

### Curl Command
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "tenantCode": "AURA_AYURVEDA",
    "email": "customer@example.com",
    "password": "Password@123"
  }'
```

### Sample Response (`200 OK`)
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlNGE4ZjljMS00YjJhLTQzZDktOWY3YS04YjFjMmQzZTRmNWEiLCJpc3MiOiJhdXJhbWFya2V0LWlkZW50aXR5IiwiYXVkIjpbImF1cmFtYXJrZXQtYXBpIl0sImlhdCI6MTcyNzI3NTc0MCwiZXhwIjoxNzI3Mjc2NjQwLCJqdGkiOiJmOGE3YjZjNS0zZDJhLTQxYjktOWY4YS0xYzJkM2U0ZjVhNmIiLCJlbWFpbCI6ImN1c3RvbWVyQGV4YW1wbGUuY29tIiwidGVuYW50SWQiOiJBVVJBX0FZVVJWRURBIiwicm9sZXMiOlsiQ1VTVE9NRVIiXX0...",
    "refreshToken": "7d9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c.4e5f6a7b-8c9d-0e1f-2a3b-4c5d6e7f8a9b",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "user": {
      "id": "e4a8f9c1-4b2a-43d9-9f7a-8b1c2d3e4f5a",
      "email": "customer@example.com",
      "firstName": "Ayush",
      "lastName": "Sharma",
      "tenantId": "AURA_AYURVEDA",
      "roles": ["CUSTOMER"],
      "status": "ACTIVE"
    }
  },
  "timestamp": "2026-09-25T20:09:00Z"
}
```

---

## 3. Refresh Access Token (Token Rotation)

- **Endpoint**: `POST /api/v1/auth/refresh`
- **Access**: Public
- **HTTP Status**: `200 OK`

### Curl Command
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "7d9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c.4e5f6a7b-8c9d-0e1f-2a3b-4c5d6e7f8a9b"
  }'
```

### Sample Response (`200 OK`)
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "new-rotated-refresh-token-uuid.string",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "user": {
      "id": "e4a8f9c1-4b2a-43d9-9f7a-8b1c2d3e4f5a",
      "email": "customer@example.com",
      "tenantId": "AURA_AYURVEDA",
      "roles": ["CUSTOMER"]
    }
  }
}
```

---

## 4. Get Current User Identity (`/me`)

- **Endpoint**: `GET /api/v1/auth/me`
- **Access**: Authenticated (`Bearer <access_token>`)
- **HTTP Status**: `200 OK`

### Curl Command
```bash
curl http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### Sample Response (`200 OK`)
```json
{
  "success": true,
  "message": "User details retrieved successfully",
  "data": {
    "userId": "e4a8f9c1-4b2a-43d9-9f7a-8b1c2d3e4f5a",
    "email": "customer@example.com",
    "tenantId": "AURA_AYURVEDA",
    "roles": ["CUSTOMER"]
  }
}
```

---

## 5. Logout Session

- **Endpoint**: `POST /api/v1/auth/logout`
- **Access**: Public
- **HTTP Status**: `200 OK`

### Curl Command
```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "new-rotated-refresh-token-uuid.string"
  }'
```

---

## 6. Change Password

- **Endpoint**: `POST /api/v1/auth/change-password`
- **Access**: Authenticated (`Bearer <access_token>`)
- **HTTP Status**: `200 OK`

### Curl Command
```bash
curl -X POST http://localhost:8080/api/v1/auth/change-password \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "Password@123",
    "newPassword": "NewStrongPassword@2026"
  }'
```

---

## Standardized Error Codes & HTTP Statuses

| Error Code | HTTP Status | Description |
| :--- | :--- | :--- |
| `AUTH_INVALID_CREDENTIALS` | `401 Unauthorized` | Invalid email or password during login |
| `AUTH_ACCOUNT_LOCKED` | `403 Forbidden` | Account status is `LOCKED` |
| `AUTH_ACCOUNT_INACTIVE` | `403 Forbidden` | Account status is `INACTIVE` or `DELETED` |
| `AUTH_EMAIL_ALREADY_EXISTS` | `409 Conflict` | Email already registered under target `tenantCode` |
| `AUTH_INVALID_REFRESH_TOKEN`| `401 Unauthorized` | Refresh token is invalid or has been revoked |
| `AUTH_REFRESH_TOKEN_EXPIRED`| `401 Unauthorized` | Refresh token has expired |
| `AUTH_FORBIDDEN` | `403 Forbidden` | Insufficient role permissions |
| `AUTH_VALIDATION_ERROR` | `400 Bad Request` | Invalid payload or constraint violation |

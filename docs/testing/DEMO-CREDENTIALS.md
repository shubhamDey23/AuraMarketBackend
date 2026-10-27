# AuraMarket Ayurveda - Demo Accounts & Credentials

Below are the development demo credentials seeded in `auramarket_identity` for tenant `AURA_AYURVEDA`.

> [!IMPORTANT]
> All demo accounts use the standard development password hash for `Password@123`.

## Demo Accounts

| Role | Email | Password | Tenant ID | User ID |
| :--- | :--- | :--- | :--- | :--- |
| **Customer** | `customer.demo@auraayurveda.test` | `Password@123` | `AURA_AYURVEDA` | `11111111-1111-1111-1111-111111111112` |
| **Customer (Dev)** | `customer@auramarket.local` | `Password@123` | `AURA_AYURVEDA` | `11111111-1111-1111-1111-111111111111` |
| **Seller** | `seller2.demo@auraayurveda.test` | `Password@123` | `AURA_AYURVEDA` | `22222222-2222-2222-2222-222222222223` |
| **Seller (Dev)** | `seller@auramarket.local` | `Password@123` | `AURA_AYURVEDA` | `22222222-2222-2222-2222-222222222222` |
| **Doctor** | `doctor2.demo@auraayurveda.test` | `Password@123` | `AURA_AYURVEDA` | `33333333-3333-3333-3333-333333333334` |
| **Doctor (Dev)** | `doctor@auramarket.local` | `Password@123` | `AURA_AYURVEDA` | `33333333-3333-3333-3333-333333333333` |
| **Admin** | `admin.demo@auraayurveda.test` | `Password@123` | `AURA_AYURVEDA` | `44444444-4444-4444-4444-444444444445` |
| **Admin (Dev)** | `admin@auramarket.local` | `Password@123` | `AURA_AYURVEDA` | `44444444-4444-4444-4444-444444444444` |

## API Testing Tokens

Use `POST http://localhost:8080/api/v1/auth/login` (or directly `http://localhost:8081/api/v1/auth/login`) with JSON body:

```json
{
  "email": "customer.demo@auraayurveda.test",
  "password": "Password@123",
  "tenantId": "AURA_AYURVEDA"
}
```

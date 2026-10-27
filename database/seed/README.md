# AuraMarket Ayurveda Seed Data System

This directory contains repeatable, idempotent, deterministic seed scripts for all microservices in the AuraMarket platform.

## Directory Structure

```
database/seed/
├── README.md
├── seed-all.ps1         # PowerShell automated seed executor
├── seed-all.sql         # Master SQL script for manual psql execution
├── verify-seed.sql      # Database row count & integrity verification
├── identity/seed.sql
├── tenant/seed.sql
├── seller/seed.sql
├── catalog/seed.sql
├── inventory/seed.sql
├── cart/seed.sql
├── order/seed.sql
├── payment/seed.sql
├── consultation/seed.sql
├── notification/seed.sql
└── admin/seed.sql
```

## How to Execute Seed

### Option 1: PowerShell Script

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\seed-all.ps1
```

### Option 2: psql Command Line

```bash
psql -U postgres -f database/seed/seed-all.sql
```

## Verification

To verify database seeding:

```bash
psql -U postgres -f database/seed/verify-seed.sql
```

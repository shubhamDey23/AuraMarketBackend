# Seed All Databases Script for AuraMarket
# Dependency order: identity -> tenant -> seller -> catalog -> inventory -> cart -> order -> payment -> consultation -> notification -> admin

$ErrorActionPreference = "Stop"

$HostName = if ($env:POSTGRES_HOST) { $env:POSTGRES_HOST } else { "localhost" }
$Port = if ($env:POSTGRES_PORT) { $env:POSTGRES_PORT } else { "5432" }
$User = if ($env:POSTGRES_USER) { $env:POSTGRES_USER } else { "postgres" }
$PsqlBin = "E:\postgreSQL\bin\psql.exe"

if (-not (Test-Path $PsqlBin)) {
    $PsqlCmd = Get-Command psql -ErrorAction SilentlyContinue
    if ($PsqlCmd) { $PsqlBin = $PsqlCmd.Source }
}

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host " AURAMARKET DATABASE SEEDER" -ForegroundColor Cyan
Write-Host " Target Host: $HostName:$Port | User: $User" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$SeedPlan = @(
    @{ Service = "identity";     Db = "auramarket_identity";     File = "database/seed/identity/seed.sql" },
    @{ Service = "tenant";       Db = "auramarket_tenant";       File = "database/seed/tenant/seed.sql" },
    @{ Service = "seller";       Db = "auramarket_seller";       File = "database/seed/seller/seed.sql" },
    @{ Service = "catalog";      Db = "auramarket_catalog";      File = "database/seed/catalog/seed.sql" },
    @{ Service = "inventory";    Db = "auramarket_inventory";    File = "database/seed/inventory/seed.sql" },
    @{ Service = "cart";         Db = "auramarket_cart";         File = "database/seed/cart/seed.sql" },
    @{ Service = "order";        Db = "auramarket_order";        File = "database/seed/order/seed.sql" },
    @{ Service = "payment";      Db = "auramarket_payment";      File = "database/seed/payment/seed.sql" },
    @{ Service = "consultation"; Db = "auramarket_consultation"; File = "database/seed/consultation/seed.sql" },
    @{ Service = "notification"; Db = "auramarket_notification"; File = "database/seed/notification/seed.sql" },
    @{ Service = "admin";        Db = "auramarket_admin";        File = "database/seed/admin/seed.sql" }
)

$RootDir = "E:\AuraMarket\AuraMarket_backend"

foreach ($step in $SeedPlan) {
    $svc = $step.Service
    $db = $step.Db
    $relFile = $step.File
    $fullPath = Join-Path $RootDir $relFile

    Write-Host "[SEEDING] $svc -> Database: $db ..." -NoNewline

    if (-not (Test-Path $fullPath)) {
        Write-Host " [FAILED] File missing: $fullPath" -ForegroundColor Red
        exit 1
    }

    try {
        & $PsqlBin -h $HostName -p $Port -U $User -d $db -f $fullPath 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host " [SUCCESS]" -ForegroundColor Green
        } else {
            Write-Host " [FAILED] Exit Code: $LASTEXITCODE" -ForegroundColor Red
            exit 1
        }
    } catch {
        Write-Host " [ERROR] $_" -ForegroundColor Red
        exit 1
    }
}

Write-Host "=========================================" -ForegroundColor Green
Write-Host " ALL SEEDS COMPLETED SUCCESSFULLY!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green

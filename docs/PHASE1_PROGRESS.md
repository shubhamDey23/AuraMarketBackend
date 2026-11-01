# Phase 1 Backend Progress

Branch: `feature/phase-1-api-integration`

## Completed in this branch

- Corrected API Gateway routing for seller catalogue and admin catalogue, inventory, and order endpoints so specific routes precede the general admin route.
- Made the root infrastructure Compose file's PostgreSQL, Redis, Kafka host port, and Kafka UI port configurable.
- Changed the PostgreSQL bootstrap database to `postgres` because `infrastructure/postgres/init-databases.sql` creates the per-service databases.
- Corrected PostgreSQL Compose health-check variable escaping.
- Moved Kafka UI's host port to `8092`; consultation service remains on `8089`.
- Aligned service datasource password defaults and PostgreSQL host overrides with the local environment configuration.
- Removed the hardcoded JWT signing-secret fallback and changed actuator health detail visibility to `when_authorized`.
- Added `docs/DEVELOPMENT_GUIDE.md` with prerequisites, infrastructure startup, service startup, health checks, testing, and current limitations.
- Updated the root README's port/password details and linked the development guide.
- Added Flyway migration and DTO/entity fields for featured/trending products and featured categories/brands.
- Added public catalogue search, featured, trending, and discounted-product filters; public browsing is forced to ACTIVE products.
- Added tenant-scoped admin and seller product-list endpoints that include non-active statuses for management screens.
- Added Postman requests for required tenant-scoped catalog listing, search, featured/trending/discounted products, categories, brands, and admin/seller listings.

## Verification status

- GitHub changes have been committed to this feature branch.
- Repository and branch reads/writes verified through the GitHub integration.
- A full Maven build, Docker Compose startup, database migration run, and live Postman run have **not** been executed in this environment. Do not treat these changes as runtime-tested until those checks pass.
- YAML syntax should be checked with `docker compose config` before starting the stack.

## Next backend work

1. Add and verify tenant-scoped admin/seller catalogue listing and product-status management.
2. Add tests for new catalogue filters/listing APIs and verify controller routes and gateway paths against the Postman collection.
3. Review authorization and tenant isolation for admin and seller endpoints.
4. Complete/verify database seed scripts and all API integration flows.
5. Add service containers and health/dependency ordering to Docker Compose after local service configuration is verified.
6. Verify Kafka producers, consumers, event schemas, and Redis cache behavior end to end.

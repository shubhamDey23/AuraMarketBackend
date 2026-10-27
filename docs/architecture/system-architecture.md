# AuraMarket Architecture Overview

## Executive Summary
AuraMarket is a multi-tenant, microservices-based e-commerce platform built with Java 21, Spring Boot 3, Spring Cloud Gateway, PostgreSQL, Redis, and Apache Kafka. 

The backend architecture is strictly decoupled from the Angular frontend (`ECommerce`), communicating exclusively over RESTful APIs via an API Gateway.

## Architectural Principles

1. **Microservices First**: Each domain service is an independent Spring Boot application with its own repository, database schema, build configuration (`pom.xml`), and deployment pipeline.
2. **Database per Service**: Zero cross-database queries or shared entities. Services communicate strictly via REST endpoints or Kafka event streams.
3. **Tenant-First Design**: The core domain engine dynamically handles multi-tenancy (e.g. `AURA_AYURVEDA`, `STYLE_HUB`). Branding, features, menus, and business logic options are served via runtime tenant configuration.
4. **Event-Driven Asynchrony**: Domain events (e.g. `order.created.v1`, `payment.completed.v1`) are published to Kafka for async notification processing, inventory updates, and analytics.

## System Diagram

```
                              +--------------------+
                              |  Angular Frontend  |
                              |   (ECommerce)      |
                              +---------+----------+
                                        |
                                        v (HTTP / REST)
                              +--------------------+
                              |    API Gateway     |
                              |    (Port 8080)     |
                              +---------+----------+
                                        |
      +---------------+-----------------+---------------+----------------+
      |               |                 |               |                |
      v               v                 v               v                v
+-----------+   +------------+   +------------+   +------------+   +------------+
| Identity  |   |   Tenant   |   |  Catalog   |   |   Seller   |   | Inventory  |
|  (8081)   |   |   (8082)   |   |   (8083)   |   |   (8084)   |   |   (8085)   |
+-----+-----+   +-----+------+   +-----+------+   +-----+------+   +-----+------+
      |               |                 |               |                |
      v               v                 v               v                v
 [DB: identity]  [DB: tenant]      [DB: catalog]   [DB: seller]     [DB: inventory]

      |               |                 |               |                |
      +---------------+-----------------+---------------+----------------+
                                        |
                                        v (Kafka Async Broker)
                              +--------------------+
                              |    Apache Kafka    |
                              +--------------------+
```

# Data Structures & Algorithms (DSA) Practical Applications in AuraMarket

This directory documents the practical data structures and algorithms applied across the microservices backend for performance optimization, search indexing, and resource management.

## Mapped Data Structures & Use Cases

### 1. Trie (Prefix Tree)
- **Use Case**: Product & Category Search Autocomplete in `catalog-service`.
- **Why**: Allows sub-millisecond prefix matching and instant term completions for high-traffic customer searches (e.g. typing "Ashwa..." resolves to "Ashwagandha Powder", "Ashwagandha Capsules").

### 2. HashMap / ConcurrentHashMap
- **Use Case**: In-memory Tenant Configuration lookup and cache routing in `tenant-service`.
- **Why**: Provides $O(1)$ constant time lookup for tenant features and feature-flag validation per request thread.

### 3. LRU Cache (Least Recently Used)
- **Use Case**: In-memory eviction policy for high-frequency catalog items before querying Redis/DB.
- **Why**: Controls memory footprint while serving top 1% trending storefront items with minimum latency.

### 4. Priority Queue (Min-Heap / Max-Heap)
- **Use Case**: Order processing fulfillment queue and Doctor Consultation Slot Allocation in `order-service` and `consultation-service`.
- **Why**: Prioritizes express shipping orders or emergency doctor appointments based on SLA timestamp deadlines.

### 5. Binary Search
- **Use Case**: Inventory batch expiry and price range filtering in `inventory-service` and `catalog-service`.
- **Why**: $O(\log N)$ lookup efficiency when matching sorted product pricing slabs or date-sorted batch records.

### 6. Graph Algorithms (BFS / DFS / Topological Sort)
- **Use Case**: Product Category Hierarchy tree traversal in `catalog-service` and Fulfillment Route Optimization in `order-service`.
- **Why**: Ensures acyclic category nesting and optimal multi-seller package routing.

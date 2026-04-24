# Guardrail API

## Overview

This project implements a backend system with strict guardrails to control bot interactions using Redis.  
PostgreSQL is used as the source of truth, while Redis acts as a real-time gatekeeper for enforcing limits and concurrency control.

---

## Setup Instructions

1. Start PostgreSQL and Redis using Docker:

docker-compose up -d

2. Run the Spring Boot application:

mvn spring-boot:run

Server runs at:
http://localhost:8080

---

## API Endpoints

- POST /api/posts  
- POST /api/posts/{postId}/comments  
- POST /api/posts/{postId}/like  

---

## Approach

- PostgreSQL stores all persistent data (posts, comments).  
- Redis is used for:
  - Real-time counters (virality score)
  - Rate limiting (bot limits)
  - Cooldowns (bot-human interaction control)
  - Notification batching  

Redis acts as a fast in-memory layer to enforce rules before data is saved.

---

## Thread Safety (Phase 2 - Atomic Locks)

Thread safety is guaranteed using Redis atomic operations:

- INCR  
  Used to maintain bot reply count per post.  
  This operation is atomic, ensuring that even under concurrent requests  
  the count never exceeds the limit (100).

- EXISTS  
  Used to check if a cooldown key already exists.  
  Prevents repeated bot interactions with the same user.

- SET with TTL  
  Used to create cooldown locks with expiration (10 minutes).  
  Ensures automatic cleanup and time-based restrictions.

Redis is single-threaded, so all operations are executed sequentially.  
This guarantees atomicity and prevents race conditions even under high concurrency  
(e.g., 200 simultaneous bot requests).

---

## Guardrails Implemented

- Horizontal Cap → max 100 bot replies per post  
- Vertical Cap → max comment depth = 20  
- Cooldown Cap → one bot-human interaction per 10 minutes  

---

## Notification Engine (Phase 3)

- If a user has already received a notification → store messages in Redis list  
- Otherwise → send notification immediately  
- A scheduled job batches and summarizes pending notifications  

---

## Postman Collection

A Postman collection JSON file is included in the repository  
to easily test all API endpoints.

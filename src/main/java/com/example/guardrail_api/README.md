# Guardrail API

## Setup

docker-compose up -d
mvn spring-boot:run

## APIs

POST /api/posts  
POST /api/posts/{postId}/comments  
POST /api/posts/{postId}/like  

## Features

- Redis virality score
- Bot limit (100)
- Depth limit (20)
- Cooldown (10 min)
- Notification batching

## Thread Safety

Used Redis atomic operations:
- INCR
- EXISTS
- TTL
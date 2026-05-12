# Java Wallet API

A backend wallet management system built using Spring Boot.  
The project supports user authentication, wallet management, money transfers, transaction history, JWT-based security, Redis caching, and Dockerized infrastructure.

This project was built to understand and implement real-world backend engineering concepts such as transactional consistency, concurrency handling, authentication, caching, and containerization.

-------------------------------------------------------------------------------------

# Features

## Authentication & Security
- User registration and login
- JWT-based authentication
- Refresh token support
- Protected APIs using Spring Security
- Password encryption using BCrypt

## Wallet Management
- Automatic wallet creation during user registration
- Add money to wallet
- Fetch wallet details
- Secure money transfer between users

## Transactions
- Transaction history support
- Pagination support for transaction APIs
- Separate transaction types:
  - CREDITLOAD
  - CREDIT
  - DEBIT

## Concurrency Handling
- Database row-level locking using `PESSIMISTIC_WRITE`
- Transaction-safe balance updates
- Prevents concurrent balance modification issues

## Caching
- Redis caching integration
- Wallet fetch API caching
- Cache eviction after balance updates

## Exception Handling
- Global exception handling
- Clean API response structure
- Validation and custom error messages

## Docker Support
- PostgreSQL container
- Redis container
- Docker Compose setup for infrastructure

-------------------------------------------------------------------------------------

# Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- Docker
- Maven

-------------------------------------------------------------------------------------

# Project Structure

src/main/java/com/wallet

├── config          -> Security & Redis configuration
├── controller      -> REST APIs
├── dto             -> Request/Response DTOs
├── exception       -> Custom exception handling
├── model           -> Database entities
├── repository      -> JPA repositories
├── security        -> JWT filters & utilities
├── service         -> Business logic

-------------------------------------------------------------------------------------

# API Overview

## Authentication APIs

| Method |    Endpoint     |    Description       |
|--------|-----------------|----------------------|
|  POST  | `/auth/register`| Register new user    |
|  POST  | `/auth/login`   | Login user           |
|  POST  | `/auth/refresh` | Refresh access token |

-------------------------------------------------------------------------------------

## Wallet APIs

| Method |      Endpoint      |     Description    |
|--------|--------------------|--------------------|
| GET    | `/wallet/walletid` | Get wallet details |
| POST   | `/wallet/add`      | Add money          |
| POST   | `/wallet/transfer` | Transfer money     |

-------------------------------------------------------------------------------------

## Transaction APIs

| Method |        Endpoint        |       Description         |
|--------|------------------------|---------------------------|
| GET    | `/wallet/transactions` | Fetch transaction history |

-------------------------------------------------------------------------------------

# API Response Format

## Success Response

json :
{
  "success": true,
  "data": {},
  "error": null
}

## Error Response

json :
{
  "success": false,
  "data": null,
  "error": "Wallet not found"
}

-------------------------------------------------------------------------------------

# How to Run the Project

## Prerequisites

Make sure the following are installed:

- Java 17+
- Maven
- Docker Desktop

-------------------------------------------------------------------------------------

# Running PostgreSQL & Redis using Docker

## Start infrastructure

docker-compose up

This starts:
- PostgreSQL
- Redis

-------------------------------------------------------------------------------------

# Application Configuration

Update `application.properties`:

//properties
spring.datasource.url=jdbc:postgresql://localhost:5432/walletdb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.redis.host=localhost
spring.redis.port=6379

spring.jpa.hibernate.ddl-auto=update

-------------------------------------------------------------------------------------

# Run Spring Boot Application

.\mvnw spring-boot:run
//mvn spring-boot:run


Application runs on:

http://localhost:8080

-------------------------------------------------------------------------------------

# Security Flow

1. User logs in
2. Access token is generated
3. Access token is sent in API headers

Example:

Authorization: Bearer <token>

-------------------------------------------------------------------------------------

# Concurrency Handling

Money transfer operations use:
- `@Transactional`
- Database locking using `PESSIMISTIC_WRITE`

This ensures:
- Consistent wallet balances
- Safe concurrent transfers

-------------------------------------------------------------------------------------

# Future Improvements

Potential enhancements:
- Kafka / RabbitMQ integration
- Notification service
- Rate limiting
- API monitoring
- Kubernetes deployment
- CI/CD pipeline
- Microservice architecture

-------------------------------------------------------------------------------------

# Learning Outcomes

This project helped in understanding:
- Spring Boot architecture
- Authentication & authorization
- Database transactions
- Concurrency handling
- Redis caching
- Dockerized development
- REST API design
- DTO-based architecture
- Exception handling

-------------------------------------------------------------------------------------

# Author

Sudarshan Tunuguntla

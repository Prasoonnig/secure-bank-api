# Secure Bank API

Secure Bank API is a Spring Boot backend for a banking application. It supports user registration, login, JWT authentication, bank account creation, deposits, withdrawals, money transfers, transaction history, and admin-only APIs.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Lombok
- Bean Validation
- Maven

## Features

- User registration
- User login
- JWT token generation
- JWT-protected APIs
- Role-based access control
- Create bank account
- View logged-in user's account
- Deposit money
- Withdraw money
- Transfer money to another account
- View transaction history
- Admin dashboard
- Admin view all users
- Admin view all accounts

## Project Structure

```text
src/main/java/com/securebank/api
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
└── service
```

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE secure_bank_db;
```

Update `src/main/resources/application.properties` if your PostgreSQL username, password, or port is different.

Current example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/secure_bank_db
spring.datasource.username=postgres
spring.datasource.password=Anshi
server.port=8081
```

## Run The Project

From the `secure-bank-api` folder:

```bash
mvn spring-boot:run
```

Base URL:

```text
http://localhost:8081
```

## API Testing Flow

### Health Check

```text
GET /api/health
```

Expected:

```text
Secure Bank API is running
```

### Register User

```text
POST /api/auth/register
```

```json
{
  "fullName": "Anshi Sharma",
  "email": "anshi@example.com",
  "password": "password123"
}
```

### Login

```text
POST /api/auth/login
```

```json
{
  "email": "anshi@example.com",
  "password": "password123"
}
```

Copy the returned JWT token and send it in protected APIs:

```text
Authorization: Bearer YOUR_TOKEN_HERE
```

### Create Account

```text
POST /api/accounts
```

Body:

```json
{}
```

### View My Account

```text
GET /api/accounts/me
```

### Deposit

```text
POST /api/accounts/deposit
```

```json
{
  "amount": 500.00
}
```

### Withdraw

```text
POST /api/accounts/withdraw
```

```json
{
  "amount": 100.00
}
```

### Transfer

```text
POST /api/accounts/transfer
```

```json
{
  "receiverAccountNumber": "RECEIVER_ACCOUNT_NUMBER",
  "amount": 100.00
}
```

### Transaction History

```text
GET /api/accounts/transactions
```

## Admin APIs

To test admin APIs, update a user role in PostgreSQL:

```sql
UPDATE users
SET role = 'ADMIN'
WHERE email = 'anshi@example.com';
```

Login again after changing the role so the new JWT contains `ADMIN`.

### Admin Dashboard

```text
GET /api/admin/dashboard
```

### View All Users




### View All Accounts

```text
GET /api/admin/accounts
```

## Security Notes

- Passwords are encrypted using BCrypt.
- Protected APIs require JWT authentication.
- Admin APIs require `ADMIN` role.
- Normal users cannot access `/api/admin/**`.
- CORS is enabled for future React frontend development on:
  - `http://localhost:3000`
  - `http://localhost:5173`

## Main API Summary

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/users/me
POST /api/accounts
GET  /api/accounts/me
POST /api/accounts/deposit
POST /api/accounts/withdraw
POST /api/accounts/transfer
GET  /api/accounts/transactions
GET  /api/admin/dashboard
GET  /api/admin/users
GET  /api/admin/accounts
```

## Status

Backend is complete and ready for GitHub push.

# Secure Bank API Backend Test Checklist

Base URL:

```text
http://localhost:8081
```

## 1. Health

```text
GET /api/health
```

Expected:

```text
Secure Bank API is running
```

## 2. Register Users

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

Create another user:

```json
{
  "fullName": "Rahul Sharma",
  "email": "rahul@example.com",
  "password": "password123"
}
```

## 3. Login

```text
POST /api/auth/login
```

```json
{
  "email": "anshi@example.com",
  "password": "password123"
}
```

Copy the `token`.

## 4. Protected User Profile

```text
GET /api/users/me
```

Header:

```text
Authorization: Bearer TOKEN
```

## 5. Create Account

```text
POST /api/accounts
```

Header:

```text
Authorization: Bearer TOKEN
```

Body:

```json
{}
```

## 6. View My Account

```text
GET /api/accounts/me
```

## 7. Deposit

```text
POST /api/accounts/deposit
```

```json
{
  "amount": 500.00
}
```

## 8. Withdraw

```text
POST /api/accounts/withdraw
```

```json
{
  "amount": 100.00
}
```

## 9. Transfer

Create an account for the second user, then transfer to that account number:

```text
POST /api/accounts/transfer
```

```json
{
  "receiverAccountNumber": "RECEIVER_ACCOUNT_NUMBER",
  "amount": 100.00
}
```

## 10. Transaction History

```text
GET /api/accounts/transactions
```

## 11. Admin Setup

In PostgreSQL:

```sql
UPDATE users
SET role = 'ADMIN'
WHERE email = 'anshi@example.com';
```

Login again after this change to get a new ADMIN token.

## 12. Admin APIs

```text
GET /api/admin/dashboard
GET /api/admin/users
GET /api/admin/accounts
```

Header:

```text
Authorization: Bearer ADMIN_TOKEN
```

## Expected Security Results

- No token on protected APIs returns `401`.
- USER token on admin APIs returns `403`.
- ADMIN token on admin APIs succeeds.
- Passwords are never returned in API responses.

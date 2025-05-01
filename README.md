# 💳 Digital Wallet API

A secure and extensible digital wallet API built with Java and Spring Boot.

Customers can manage their own wallets, deposits, and withdrawals. Employees can perform operations on behalf of any customer.

---

## 🚀 Features

- ✅ Create wallets with different currencies (TRY, USD, EUR)
- ✅ Deposit and withdraw money with business rules
- ✅ Approve or deny large transactions
- ✅ Role-based access control (CUSTOMER vs EMPLOYEE)
- ✅ Integrated Swagger documentation for API testing
- ✅ H2 in-memory database
- ✅ Unit and integration test coverage
- ✅ Secure endpoints with Spring Security

---

## 🧰 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA (H2)
- Lombok
- Swagger (SpringDoc OpenAPI)
- JUnit 5 & Mockito
- Maven

---

## 📦 Getting Started

### 1. Clone the repository
```bash
git clone <repo-url>
cd digital-wallet
./mvnw clean install
```

### 2. Run the application
```bash
./mvnw spring-boot:run
```

### 3. Access the H2 database console
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:digitalwallet`
- Username: `digitalwallet`
- Password: (leave empty)

---

## 🔐 Default Users

| Role      | Username       | Password       |
|-----------|----------------|----------------|
| CUSTOMER  | `12345678901`  | `12345678901`  |
| EMPLOYEE  | `employee1`    | `employee1`    |

> - CUSTOMERS can only manage their own wallets and transactions
> - EMPLOYEES can access and manage all customer data

---

## 📘 Business Rules

### Deposits
- Amount ≤ 1000 → Automatically `APPROVED`
- Amount > 1000 → Saved as `PENDING`
    - **APPROVED** → added to both `balance` and `usableBalance`
    - **PENDING** → added only to `balance`

### Withdrawals
- Amount ≤ 1000 → Automatically `APPROVED`
- Amount > 1000 → Saved as `PENDING`
    - **APPROVED** → deducted from both `balance` and `usableBalance`
    - **PENDING** → deducted only from `usableBalance`

### Approvals
- `APPROVED` → Applies the transaction effect to wallet
- `DENIED` → Cancels the transaction, reverts balances if necessary

---

## 📂 API Documentation (Swagger)

> Accessible at:
```
http://localhost:8080/swagger-ui/index.html
```

### 🧪 Sample API Calls

#### Create Wallet
```http
POST /api/wallets/create/{customerId}
Content-Type: application/json

{
  "walletName": "My Wallet",
  "currency": "TRY",
  "activeForShopping": true,
  "activeForWithdraw": true
}
```

#### Deposit
```http
POST /api/transactions/deposit
{
  "walletId": 1,
  "amount": 1500,
  "oppositePartyType": "IBAN",
  "oppositeParty": "TR123..."
}
```

#### Withdraw
```http
POST /api/transactions/withdraw
{
  "walletId": 1,
  "amount": 500,
  "oppositePartyType": "PAYMENT",
  "oppositeParty": "PAY_ABC_001"
}
```

#### Approve/Deny
```http
POST /api/transactions/approve
{
  "transactionId": 10,
  "status": "APPROVED"
}
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

Includes:

- ✅ Unit tests for services
- ✅ Integration tests for controllers and repositories

---

## 🧱 Project Structure

```
com.digitalwallet
├── controller         # REST endpoints
├── dto                # Data Transfer Objects
├── service            # Business logic
├── repository         # JPA repositories
├── entity             # Domain models
├── security           # Spring Security config
├── config             # Swagger, database configs
└── util               # Common utility classes
```

---

## 🛡 Security Overview

- Basic Authentication
- Dynamic login:
    - CUSTOMERS are authenticated via TCKN (used as both username & password)
    - EMPLOYEES are authenticated via in-memory credentials
- Method-level access control with role checks

---

## ⚠️ Notes

- Passwords are not encoded – this is for development only.
- Production usage should replace this with `BCryptPasswordEncoder` or JWT-based auth.
- Database is in-memory; use PostgreSQL or MySQL for persistence.

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Contributing

Feel free to fork and submit pull requests. Feedback is always welcome!
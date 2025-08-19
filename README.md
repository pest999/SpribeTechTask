# PlayerController API

Spring Boot REST API for user management.
---

## 🎯 Features

- ✅ **CRUD operations** for users
- ✅ **Data validation** with detailed error messages
- ✅ **REST API** with proper HTTP status codes
- ✅ **Docker support** for PostgreSQL
- ✅ **Comprehensive logging** with file rotation
---

## 🚀 Quick Start

### Requirements
- Java 17+
- Maven 3.6+

### Running the Application

#### H2 Database (Recommended - Simple Setup)
```bash
# Start application with H2 in-memory database
./dev-start-h2.bat

# Or manually
mvn spring-boot:run
```

#### PostgreSQL Database (Docker - NOT IMPLEMENTED)
```bash
# ⚠️ WARNING: Docker setup has timezone issues in non-English Windows environments
# PostgreSQL timezone "Europe/Kiev" causes fatal errors
# Use H2 database instead for stable operation

# NOT RECOMMENDED - may fail:
./dev-start-postgres.bat
./run-tests.bat
```

#### API Testing (Automation)
```bash
# H2 Database Tests (Recommended)
./run-tests-h2.bat

# PostgreSQL Tests (May fail due to timezone issues)
./run-tests.bat
```

**API available at:** http://localhost:8082
**H2 Console:** http://localhost:8082/h2-console

---

## 📋 API Endpoints

| Method | Endpoint | Description | Permissions |
|--------|----------|-------------|-------------|
| `POST` | `/player/create` | Create user |
| `GET` | `/player/get/{playerId}` | Get user by ID | all |
| `PATCH` | `/player/update/{id}` | Update user (partial) |
| `DELETE` | `/player/delete` | Delete user |
| `GET` | `/player/get/all` | Get all users | all |

---

## 📊 Data Models

### Create User Request
```json
{
  "login": "string",
  "screenName": "string", 
  "password": "string",
  "age": "int",
  "gender": "male",
  "role": "user"
}
```

### Create Response (with password)
```json
{
  "id": 1,
  "login": "testuser",
  "screenName": "Test User",
  "password": "password123",
  "age": 25,
  "gender": "male",
  "role": "user"
}
```

### Update Response (without password)
```json
{
  "id": 1,
  "login": "testuser",
  "screenName": "Updated Name",
  "age": 26,
  "gender": "male",
  "role": "user"
}
```

---

## ✅ Validation Rules

### Password
- **Length:** 7-15 characters
- **Requirements:** must contain letters AND digits
- **Pattern:** `^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{7,15}$`

### Age
- **Range:** 16-60 years (inclusive)

### Gender
- **Values:** `male` or `female` (lowercase)

### Uniqueness
- **login** - must be unique
- **screenName** - must be unique

---

## 🗄️ Database Configuration

### PostgreSQL (Testing)
```yaml
# docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: flamingo_db
      POSTGRES_USER: flamingo_user
      POSTGRES_PASSWORD: flamingo_pass
    ports:
      - "5432:5432"
```

### Initial Data
```sql

-- Admin  
id=1, login='admin', role='ADMIN'
```

---

## 📝 Logging System

### Log Files (with rotation)
- `logs/player-api.log` - Main application log
- `logs/player-api-errors.log` - Errors only
- `logs/player-api-startup.log` - Startup/shutdown logs
- **Maximum 20 files per day** with automatic rotation

### What's Logged
- Application startup with database type detection
- All CRUD operations with parameters
- Permission validations and business logic
- Errors and exceptions with full context

### Database Type Detection
```log
# PostgreSQL Database  
Database Type: PostgreSQL Database
Database Mode: Persistent (Test/Production)
```

---

## 🧪 API Testing Framework

### Quick Start
```bash
# Run all tests with Allure reports
.\run-tests.bat

# Run tests only (without Allure)
mvn test -Dspring.profiles.active=postgresql
```

### Test Features
- ✅ **Parallel execution** - Multiple tests run concurrently
- ✅ **Thread-safe** - Each test has isolated data context
- ✅ **Allure reporting** - Beautiful HTML reports with steps
- ✅ **Database cleanup** - Automatic cleanup after all tests
- ✅ **PostgreSQL integration** - Tests run against real database

### Test Structure
```
src/test/java/com/playermanagement/
├── tests/          # Test classes
├── api/            # API call wrappers
├── model/          # Request/Response POJOs
├── utils/          # Test data builders
├── config/         # Test configuration
└── core/           # Thread-safe test context
```

### Allure Reports
After running tests, Allure automatically opens interactive reports with:
- Test execution timeline
- Step-by-step API calls
- Request/Response details
- Screenshots and attachments

---

## 🧪 Manual Testing Examples

**Create User:**
```bash
curl -X POST http://localhost:8082/player/create \
  -H "Content-Type: application/json" \
  -d '{
    "login": "newuser",
    "screenName": "New User", 
    "password": "password123",
    "age": 25,
    "gender": "male",
    "role": "user"
  }'
```

**Get User:**
```bash
curl http://localhost:8082/player/get/1
```

**Update User:**
```bash
curl -X PATCH http://localhost:8082/player/update/1 \
  -H "Content-Type: application/json" \
  -d '{"age": 26}'
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8082/player/delete \
  -H "Content-Type: application/json" \
  -d '{"playerId": 1}'
```

---


### Technologies
- **Spring Boot 2.7.0** - Main framework
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Database
- **JUnit 5** - Testing framework
- **RestAssured** - API testing
- **Allure** - Test reporting
- **Lombok** - Reduce boilerplate code
- **Docker** - Containerization

---

## 📈 HTTP Status Codes

| Code | Description | When Used |
|------|-------------|-----------|
| `201` | Created | Successful user creation |
| `200` | OK | Successful GET/PATCH/DELETE operations |
| `400` | Bad Request | Validation errors |
| `403` | Forbidden | Insufficient permissions |
| `404` | Not Found | User not found |

---

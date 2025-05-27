# PlayerController API

Spring Boot REST API for user management with role-based permission system.

**🔗 Reference API:** [Swagger UI](http://3.68.165.45/swagger-ui.html#/player-controller)

---

## 🎯 Features

- ✅ **CRUD operations** for users
- ✅ **Role-based permissions** (supervisor/admin/user)
- ✅ **Data validation** with detailed error messages
- ✅ **Dual database support** (H2 for development, PostgreSQL for testing)
- ✅ **REST API** with proper HTTP status codes
- ✅ **Docker support** for PostgreSQL
- ✅ **Comprehensive logging** with file rotation

---

## 🚀 Quick Start

### Requirements
- Java 11+
- Maven 3.6+
- Docker (optional for PostgreSQL)

### Running the Application

#### H2 Database (Development)
```bash
# Using batch script (10-minute auto-shutdown)
./dev-start-h2.bat

# Or manually
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### PostgreSQL Database (Development)
```bash
# Using batch script (10-minute auto-shutdown)  
./dev-start-postgres.bat

# Or manually
docker-compose up -d
mvn spring-boot:run -Dspring.profiles.active=test
```

#### API Testing (Automation)
```bash
# Full test automation: build → start → test → report
./run-tests.bat
```

**API available at:** http://localhost:8082

---

## 📋 API Endpoints

| Method | Endpoint | Description | Permissions |
|--------|----------|-------------|-------------|
| `POST` | `/player/create/{editor}` | Create user | supervisor, admin |
| `GET` | `/player/get/{playerId}` | Get user by ID | all |
| `PATCH` | `/player/update/{editor}/{id}` | Update user (partial) | role-dependent |
| `DELETE` | `/player/delete/{editor}` | Delete user | supervisor, admin |
| `GET` | `/player/get/all` | Get all users | all |

---

## 👥 Role System

### Supervisor
- ✅ Can create admin/user
- ✅ Can edit anyone
- ✅ Can delete all (except other supervisors)
- ❌ Cannot be deleted

### Admin  
- ✅ Can create admin/user
- ✅ Can edit users + self
- ✅ Can delete users only
- ✅ Can be deleted by supervisor

### User
- ❌ Cannot create users
- ✅ Can edit only self
- ❌ Cannot delete anyone
- ✅ Can be deleted by admin/supervisor

---

## 📊 Data Models

### Create User Request
```json
{
  "login": "string",
  "screenName": "string", 
  "password": "string",
  "age": 25,
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

### H2 (Development)
- In-memory database
- Web console: http://localhost:8082/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`, Password: (empty)

### PostgreSQL (Testing)
```yaml
# docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: stribe_db
      POSTGRES_USER: stribe_user
      POSTGRES_PASSWORD: stribe_pass
    ports:
      - "5432:5432"
```

### Initial Data
```sql
-- Supervisor
id=1, login='supervisorLogin', role='SUPERVISOR'

-- Admin  
id=2, login='admin', role='ADMIN'

-- User
id=3, login='user', role='USER'
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
# H2 Database
Database Type: H2 In-Memory Database
Database Mode: In-Memory (Development)

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
curl -X POST http://localhost:8082/player/create/admin \
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
curl -X PATCH http://localhost:8082/player/update/admin/1 \
  -H "Content-Type: application/json" \
  -d '{"age": 26}'
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8082/player/delete/admin \
  -H "Content-Type: application/json" \
  -d '{"playerId": 1}'
```

---

## 🏗️ Project Structure

```
src/main/java/com/playermanagement/
├── controller/     # REST Controllers
├── service/        # Business Logic  
├── repository/     # Data Access Layer
├── model/          # Entity Classes
├── dto/            # Data Transfer Objects
├── mapper/         # DTO-Entity Mapping
├── exception/      # Custom Exceptions
└── validation/     # Validation Utilities
```

### Technologies
- **Spring Boot 2.7.0** - Main framework
- **Spring Data JPA** - Database operations
- **H2 Database** - Development
- **PostgreSQL** - Testing
- **JUnit 5** - Testing framework
- **RestAssured** - API testing
- **Allure** - Test reporting
- **Lombok** - Reduce boilerplate code
- **Docker** - Containerization
- **Logback** - Logging with file rotation

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

## 🔐 Security Notes

### Current Level
- ✅ Input data validation
- ✅ Role-based permissions
- ✅ Unique field constraints

### Limitations
- ⚠️ Passwords stored in plain text
- ⚠️ No authentication mechanism
- ⚠️ Passwords returned in some responses

### Production Requirements
1. Password encryption (BCrypt)
2. JWT authentication
3. HTTPS
4. Rate limiting

---

## 📝 License

This project was created for AQA Java Engineer technical assignment.

---

**Reference API:** http://3.68.165.45/swagger-ui.html#/player-controller 
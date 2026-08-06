# Architecture Documentation

## Overview

This login application follows a clean architecture with clear separation of concerns, implementing the Repository and Service patterns to ensure maintainability, testability, and scalability.

## Architecture Layers

### 1. Presentation Layer (Templates & Static)
- **Location**: `templates/`, `static/`
- **Responsibility**: User interface rendering
- **Technology**: HTML5, Bootstrap 5.3, JavaScript ES6+
- **Components**:
  - `login.html`: Authentication form with CSRF protection
  - `dashboard.html`: Protected user dashboard

### 2. API Layer (Controllers)
- **Location**: `app/api/`
- **Responsibility**: HTTP request/response handling, routing
- **Technology**: Flask Blueprints
- **Components**:
  - `auth_controller.py`: Authentication endpoints
- **Features**:
  - Request validation
  - Response formatting
  - HTTP status code management
  - Session management

### 3. Service Layer (Business Logic)
- **Location**: `app/services/`
- **Responsibility**: Business rules and orchestration
- **Components**:
  - `auth_service.py`: Authentication logic, account lockout
- **Features**:
  - Password verification
  - Failed attempt tracking
  - Account lockout management
  - Forgot password workflow

### 4. Repository Layer (Data Access)
- **Location**: `app/repositories/`
- **Responsibility**: Database operations
- **Components**:
  - `user_repository.py`: User CRUD operations
- **Features**:
  - SQL query execution
  - Data mapping (row to model)
  - Connection management

### 5. Model Layer (Domain Models)
- **Location**: `app/models/`
- **Responsibility**: Data structures and domain entities
- **Components**:
  - `user.py`: User entity
- **Features**:
  - Data encapsulation
  - Business methods (e.g., `is_locked()`)
  - Serialization (`to_dict()`)

### 6. Validation Layer
- **Location**: `app/validators/`
- **Responsibility**: Input validation and sanitization
- **Components**:
  - `auth_validator.py`: Authentication input validation
- **Features**:
  - Required field validation
  - Format validation (email)
  - Error message generation

### 7. Middleware Layer
- **Location**: `app/middleware/`
- **Responsibility**: Cross-cutting concerns
- **Components**:
  - `rate_limiter.py`: Rate limiting for API endpoints
- **Features**:
  - Request throttling
  - IP-based tracking
  - Sliding window algorithm

## Design Patterns

### 1. Repository Pattern
Abstracts data access logic from business logic.

**Benefits**:
- Testability (can mock repositories)
- Database independence
- Centralized data access logic

**Implementation**:
```python
# app/repositories/user_repository.py
class UserRepository:
    def find_by_username(self, username):
        # Database query logic
        pass
```

### 2. Service Pattern
Encapsulates business logic separate from controllers.

**Benefits**:
- Reusable business logic
- Testable without HTTP layer
- Single responsibility

**Implementation**:
```python
# app/services/auth_service.py
class AuthService:
    def authenticate(self, username, password):
        # Authentication logic
        pass
```

### 3. Factory Pattern
Creates Flask application with proper configuration.

**Benefits**:
- Flexible configuration
- Testability (can create test app)
- Extension initialization

**Implementation**:
```python
# app/__init__.py
def create_app():
    app = Flask(__name__)
    # Configuration and initialization
    return app
```

### 4. Decorator Pattern
Used for rate limiting and route protection.

**Implementation**:
```python
@rate_limiter.limit(max_requests=10, window_seconds=60)
def login():
    pass
```

## Data Flow

### Login Request Flow

```
1. User submits form (templates/login.html)
   ↓
2. JavaScript sends POST to /api/login (CSRF token included)
   ↓
3. auth_controller.login() receives request
   ↓
4. Rate limiter middleware checks request count
   ↓
5. Request data extracted and passed to service
   ↓
6. auth_service.authenticate() validates credentials
   ↓
7. user_repository.find_by_username() queries database
   ↓
8. Password verification using Werkzeug
   ↓
9. Failed attempts tracked or reset
   ↓
10. Response formatted and returned to client
   ↓
11. JavaScript handles response (redirect or error display)
```

### Account Lockout Flow

```
1. User fails authentication
   ↓
2. auth_service increments failed_attempts
   ↓
3. If failed_attempts >= 3:
   - Calculate locked_until (current time + 10 seconds)
   - user_repository.update_failed_attempts()
   ↓
4. Next login attempt:
   - user.is_locked() checks if locked_until > now
   - Return locked error if true
   ↓
5. After timeout:
   - is_locked() returns false
   - User can attempt login again
```

## Security Architecture

### 1. CSRF Protection
- **Implementation**: Flask-WTF CSRFProtect
- **Scope**: All POST/PUT/DELETE requests
- **Mechanism**: Token validation in forms and AJAX

### 2. Password Security
- **Hashing**: Werkzeug's scrypt algorithm
- **Salting**: Automatic per-password unique salt
- **Verification**: Constant-time comparison

### 3. Session Security
- **Storage**: Server-side (Flask session)
- **Cookies**: HTTPOnly, SameSite=Lax
- **Timeout**: 30 minutes of inactivity
- **SECRET_KEY**: Loaded from environment

### 4. Rate Limiting
- **Endpoints**: /api/login (10 req/min), /api/forgot-password (5 req/min)
- **Tracking**: IP-based
- **Window**: Sliding window algorithm

### 5. Account Protection
- **Lockout**: 3 failed attempts
- **Duration**: 10 seconds
- **Auto-unlock**: Automatic after timeout

### 6. Input Validation
- **Layer**: Validator classes
- **Checks**: Required fields, format validation
- **Sanitization**: Strip whitespace (username/email)

## Database Schema

### Users Table

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    failed_attempts INTEGER DEFAULT 0,
    locked_until TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- Indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_locked_until ON users(locked_until);
```

## Configuration Management

### Environment Variables
All sensitive configuration loaded from environment:

```
SECRET_KEY - Flask secret key for sessions
DATABASE_PATH - Path to SQLite database
PORT - Application port (default: 5000)
DEBUG - Debug mode flag (default: False)
SESSION_COOKIE_SECURE - HTTPS-only cookies (production: True)
ENV - Environment name (production/development)
```

### Configuration Hierarchy
1. Environment variables (highest priority)
2. .env file (local development)
3. Default values (fallback)

## Logging Architecture

### Log Levels
- **INFO**: Successful operations (login, logout)
- **WARNING**: Security events (failed attempts, lockouts)
- **ERROR**: Exceptions and failures

### Log Format
```
%(asctime)s - %(name)s - %(levelname)s - %(message)s
```

### Log Storage
- **File**: `logs/app.log`
- **Console**: stdout (for development)

### Logged Events
- Successful logins
- Failed login attempts
- Account lockouts
- Forgot password requests
- Rate limit violations

## Error Handling

### Exception Strategy
1. **Try-Catch at Controller**: Catch all exceptions
2. **Logging**: Log error with stack trace
3. **User Response**: Generic error message (no details leaked)
4. **HTTP Status**: Appropriate status code

### Error Response Format
```json
{
  "success": false,
  "errors": ["User-friendly error message"]
}
```

## Testing Strategy

### Unit Tests
- **Target**: Individual classes/functions
- **Mocking**: Mock dependencies (repositories, services)
- **Location**: `tests/unit/`

### Integration Tests
- **Target**: Multiple components together
- **Database**: Test database
- **Location**: `tests/integration/`

### API Tests
- **Target**: HTTP endpoints
- **Method**: Flask test client
- **Location**: `tests/api/`

## Scalability Considerations

### Current Architecture
- **Database**: SQLite (suitable for small-medium load)
- **Session**: In-memory (single server)
- **Rate Limiting**: In-memory (single server)

### Scaling Path
1. **Database**: Migrate to PostgreSQL/MySQL
2. **Sessions**: Use Redis or database-backed sessions
3. **Rate Limiting**: Use Redis for distributed rate limiting
4. **Horizontal Scaling**: Add load balancer, multiple app servers
5. **Caching**: Add Redis for user lookup caching

## Maintenance

### Code Organization
- **Modularity**: Each component in its own module
- **Naming**: Clear, descriptive names
- **Documentation**: Docstrings for all public methods

### Dependency Management
- **requirements.txt**: Pinned versions for reproducibility
- **Updates**: Regular security updates

### Database Migrations
- **Schema**: SQL files in `database/schema/`
- **Versioning**: Numbered files (001_, 002_, etc.)
- **Execution**: Manual via scripts

## Future Enhancements

### Phase 2 (Planned)
- Email integration for forgot password
- Password strength requirements
- Multi-factor authentication (MFA)
- OAuth integration (Google, GitHub)

### Phase 3 (Planned)
- User registration
- Email verification
- Password reset flow
- Account recovery options

### Phase 4 (Planned)
- Role-based access control (RBAC)
- Audit trail for all actions
- Admin panel
- User profile management

## Compliance & Standards

### OWASP Top 10 Compliance
✅ A01: Broken Access Control - Session management  
✅ A02: Cryptographic Failures - Password hashing  
✅ A03: Injection - Parameterized queries  
✅ A05: Security Misconfiguration - Environment variables  
✅ A07: Authentication Failures - Lockout protection  

### Best Practices
✅ SOLID principles  
✅ Clean architecture  
✅ Separation of concerns  
✅ Dependency injection  
✅ Input validation  
✅ Output encoding  
✅ Error handling  
✅ Logging and monitoring

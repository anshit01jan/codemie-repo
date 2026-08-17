# Login Application

A secure, enterprise-grade login application built with Flask, Bootstrap, and SQLite featuring account lockout protection, CSRF protection, rate limiting, and forgot password functionality.

## Features

- **Secure Authentication**: Username/password login with password hashing
- **Account Lockout**: Automatically locks account after 3 failed login attempts for 10 seconds
- **Forgot Password**: Email-based password recovery flow
- **CSRF Protection**: All forms protected against Cross-Site Request Forgery
- **Rate Limiting**: API endpoints protected against brute force attacks
- **Session Management**: Secure session handling with HTTPOnly cookies
- **Audit Logging**: Comprehensive logging of authentication events
- **Clean Architecture**: Repository and Service pattern implementation

## Technology Stack

### Frontend
- Bootstrap 5.3.0
- Vanilla JavaScript (ES6+)

### Backend
- Python 3.12+
- Flask 3.0.0
- Flask-WTF (CSRF Protection)
- Werkzeug (Password Hashing)

### Database
- SQLite

## Project Structure

```
codemie-project/
├── app/
│   ├── api/                    # API controllers
│   │   └── auth_controller.py
│   ├── services/               # Business logic layer
│   │   └── auth_service.py
│   ├── repositories/           # Data access layer
│   │   └── user_repository.py
│   ├── models/                 # Data models
│   │   └── user.py
│   ├── validators/             # Input validation
│   │   └── auth_validator.py
│   ├── middleware/             # Rate limiting, etc.
│   │   └── rate_limiter.py
│   └── __init__.py            # Flask app factory
├── database/
│   ├── schema/                 # Database schema
│   │   └── 001_create_users_table.sql
│   └── seed/                   # Seed data
│       └── 001_seed_users.sql
├── templates/                  # HTML templates
│   ├── login.html
│   └── dashboard.html
├── tests/                      # Test suites
│   ├── unit/
│   ├── integration/
│   └── api/
├── scripts/                    # Utility scripts
│   └── init_db.py
├── docs/                       # Documentation
├── logs/                       # Application logs
├── .env.example               # Environment variables template
├── .gitignore
├── requirements.txt
├── README.md
└── run.py                     # Application entry point
```

## Installation

### Prerequisites

- Python 3.12 or higher
- pip (Python package manager)

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/anshit01jan/codemie-repo.git
   cd codemie-project
   ```

2. **Create a virtual environment**
   ```bash
   python -m venv venv
   ```

3. **Activate the virtual environment**
   - Windows:
     ```bash
     venv\Scripts\activate
     ```
   - Linux/Mac:
     ```bash
     source venv/bin/activate
     ```

4. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

5. **Configure environment variables**
   ```bash
   cp .env.example .env
   ```
   Edit `.env` and set your SECRET_KEY:
   ```
   SECRET_KEY=your-unique-secret-key-here
   ```

6. **Initialize the database**
   ```bash
   python scripts/init_db.py
   ```

7. **Run the application**
   ```bash
   python run.py
   ```

8. **Access the application**
   Open your browser and navigate to: `http://localhost:5000`

## Default Credentials

The application comes with a pre-configured test user:

- **Username**: `scrum50`
- **Password**: `ScrumPass1`
- **Email**: `scrum50@example.com`

## Usage

### Login Flow

1. Navigate to the login page
2. Enter username and password
3. Click "Login"
4. On successful authentication, you'll be redirected to the dashboard

### Account Lockout

- After 2 incorrect password attempts, you'll see remaining attempts
- On the 3rd failed attempt, the account is locked for 10 seconds
- The account automatically unlocks after the timeout period

### Forgot Password

1. Click "Forgot Password?" link on the login page
2. Enter your registered email address
3. Click "Send Reset Link"
4. Success message will be displayed
5. (Note: In production, this would send an actual email)

## Security Features

### P0 - Critical
✅ **Secrets Management**: SECRET_KEY loaded from environment variables  
✅ **CSRF Protection**: All POST endpoints protected with CSRF tokens

### P1 - Medium Priority
✅ **Rate Limiting**: Authentication endpoints limited (10 req/min for login, 5 req/min for forgot password)  
✅ **Audit Logging**: Comprehensive logging of all authentication events  
✅ **Environment Configuration**: All sensitive config from environment variables  
✅ **Secure Sessions**: HTTPOnly, SameSite=Lax cookies with 30-minute timeout

### P2 - Low Priority
✅ **Bootstrap SRI**: Subresource Integrity for CDN resources  
✅ **Password Preservation**: Whitespace preserved in password fields  
✅ **Clean Dependencies**: Only required packages included

## API Endpoints

### POST /api/login
Login with username and password

**Request:**
```json
{
  "username": "scrum50",
  "password": "ScrumPass1"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "username": "scrum50",
    "email": "scrum50@example.com"
  }
}
```

**Response (Failed - Invalid Credentials):**
```json
{
  "success": false,
  "errors": ["Invalid username or password. 2 attempt(s) remaining."]
}
```

**Response (Account Locked):**
```json
{
  "success": false,
  "errors": ["Account is locked. Please try after 8 seconds."],
  "locked": true
}
```

### POST /api/forgot-password
Request password reset link

**Request:**
```json
{
  "email": "scrum50@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Forgot Password link has been sent to registered email address."
}
```

### POST /api/logout
Logout current user

**Response:**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

## Architecture Principles

### Design Patterns
- **Repository Pattern**: Data access abstraction
- **Service Pattern**: Business logic encapsulation
- **Factory Pattern**: Flask application creation
- **Dependency Injection**: Service dependencies

### SOLID Principles
- **Single Responsibility**: Each class has one purpose
- **Open/Closed**: Extensible without modification
- **Liskov Substitution**: Proper inheritance hierarchy
- **Interface Segregation**: Focused interfaces
- **Dependency Inversion**: Depend on abstractions

### Prohibited Practices
❌ Business logic in controllers  
❌ Direct UI to database communication  
❌ Hardcoded configuration  
❌ Global mutable state  
❌ Hardcoded passwords/secrets  
❌ Sensitive data logging

## Testing

### Unit Tests
```bash
python -m pytest tests/unit
```

### Integration Tests
```bash
python -m pytest tests/integration
```

### API Tests
```bash
python -m pytest tests/api
```

### Run All Tests
```bash
python -m pytest
```

## Logging

Application logs are stored in `logs/app.log` with the following format:
```
2026-08-06 10:30:45 - app.services.auth_service - INFO - Successful login for user: scrum50
2026-08-06 10:31:12 - app.services.auth_service - WARNING - Failed login attempt for user: scrum50 (attempt 1)
2026-08-06 10:31:45 - app.services.auth_service - WARNING - Account locked for user: scrum50 until 2026-08-06 10:31:55
```

## Deployment

### Production Checklist

1. ✅ Set strong SECRET_KEY in environment
2. ✅ Set SESSION_COOKIE_SECURE=True (HTTPS only)
3. ✅ Set DEBUG=False
4. ✅ Configure proper database backups
5. ✅ Set up log rotation
6. ✅ Use reverse proxy (nginx/Apache)
7. ✅ Enable HTTPS
8. ✅ Configure firewall rules
9. ✅ Set up monitoring and alerts
10. ✅ Regular security updates

### Environment Variables (Production)

```bash
SECRET_KEY=<strong-random-key>
DATABASE_PATH=database/login_app.db
PORT=5000
DEBUG=False
SESSION_COOKIE_SECURE=True
ENV=production
```

## Troubleshooting

### Database Issues
If you encounter database errors:
```bash
python scripts/init_db.py
```

### Port Already in Use
Change the port in `.env`:
```
PORT=8000
```

### CSRF Token Errors
Clear browser cookies and refresh the page.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write tests
5. Submit a pull request

## License

This project is developed as part of CodeMie training and is for educational purposes.

## Support

For issues and questions:
- Create an issue in the GitHub repository
- Contact the development team

## Changelog

### Version 1.0.0 (2026-08-06)
- Initial release
- Basic authentication flow
- Account lockout protection
- Forgot password functionality
- CSRF protection
- Rate limiting
- Comprehensive logging
- Clean architecture implementation
- Security best practices (P0, P1, P2)

## Authors

- Development Team - CodeMie Project

## Acknowledgments

- Flask documentation and community
- Bootstrap team for the UI framework
- Security best practices from OWASP

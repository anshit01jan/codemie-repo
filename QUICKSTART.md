# Quick Start Guide

## Setup in 5 Minutes

### Step 1: Install Dependencies

```bash
pip install -r requirements.txt
```

### Step 2: Initialize Database

```bash
python scripts/init_db.py
```

### Step 3: Run Application

```bash
python run.py
```

### Step 4: Access Application

Open browser: `http://localhost:5000`

## Test Credentials

- **Username**: scrum50
- **Password**: ScrumPass1

## Test Scenarios

### Scenario 1: Successful Login

1. Enter username: `scrum50`
2. Enter password: `ScrumPass1`
3. Click "Login"
4. **Expected**: Redirected to dashboard with welcome message

### Scenario 2: Account Lockout (3 Failed Attempts)

1. Enter username: `scrum50`
2. Enter wrong password: `wrong1`
3. Click "Login"
4. **Expected**: "Invalid username or password. 2 attempt(s) remaining."

5. Enter wrong password: `wrong2`
6. Click "Login"
7. **Expected**: "Invalid username or password. 1 attempt(s) remaining."

8. Enter wrong password: `wrong3`
9. Click "Login"
10. **Expected**: "Account is locked. Please try after 10 seconds."

11. Wait 10 seconds
12. Enter correct password: `ScrumPass1`
13. Click "Login"
14. **Expected**: Successful login to dashboard

### Scenario 3: Forgot Password

1. Click "Forgot Password?" link
2. Enter email: `scrum50@example.com`
3. Click "Send Reset Link"
4. **Expected**: "Forgot Password link has been sent to registered email address."

### Scenario 4: Invalid Email Format

1. Click "Forgot Password?" link
2. Enter invalid email: `invalid-email`
3. Click "Send Reset Link"
4. **Expected**: "Invalid email format"

## Features Implemented

✅ **Authentication**
- Username/password login
- Secure password hashing (Werkzeug scrypt)
- Session management

✅ **Security**
- CSRF protection on all forms
- Rate limiting (10 req/min for login, 5 req/min for forgot password)
- HTTPOnly secure cookies
- Account lockout after 3 failed attempts
- Auto-unlock after 10 seconds

✅ **User Experience**
- Responsive Bootstrap UI
- Real-time validation feedback
- Modal for forgot password
- Clear error messages
- Success notifications

✅ **Architecture**
- Clean architecture (Repository + Service pattern)
- SOLID principles
- Separation of concerns
- Input validation layer
- Middleware for cross-cutting concerns

✅ **Logging**
- Successful login events
- Failed login attempts
- Account lockout events
- Forgot password requests
- Rate limit violations

## API Endpoints

### POST /api/login
**Request:**
```json
{
  "username": "scrum50",
  "password": "ScrumPass1"
}
```

**Success Response (200):**
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

**Failed Response (401):**
```json
{
  "success": false,
  "errors": ["Invalid username or password. 2 attempt(s) remaining."]
}
```

**Locked Response (423):**
```json
{
  "success": false,
  "errors": ["Account is locked. Please try after 8 seconds."],
  "locked": true
}
```

### POST /api/forgot-password
**Request:**
```json
{
  "email": "scrum50@example.com"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Forgot Password link has been sent to registered email address."
}
```

### POST /api/logout
**Response (200):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

## Troubleshooting

### Port 5000 Already in Use

Edit `.env` (or create one from `.env.example`):
```
PORT=8000
```

Then restart the application.

### Database Error

Reinitialize database:
```bash
python scripts/init_db.py
```

### CSRF Token Error

Clear browser cookies and refresh the page.

### Import Errors

Ensure virtual environment is activated and dependencies are installed:
```bash
pip install -r requirements.txt
```

## Development Mode

For development with auto-reload:

Create `.env`:
```
SECRET_KEY=dev-secret-key
DEBUG=True
PORT=5000
```

Run:
```bash
python run.py
```

## Next Steps

1. Read `README.md` for complete documentation
2. Review `docs/ARCHITECTURE.md` for architecture details
3. Check `docs/DEPLOYMENT.md` for production deployment
4. Explore the codebase structure
5. Add custom features as needed

## Support

For issues:
- Check logs in `logs/app.log`
- Review error messages in browser console
- Verify database exists: `database/login_app.db`
- Ensure all dependencies are installed

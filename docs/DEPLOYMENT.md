# Deployment Guide

## Prerequisites

- Python 3.12+
- pip
- Virtual environment support
- Git (for version control)

## Local Development Setup

### 1. Environment Setup

```bash
# Clone repository
git clone https://github.com/anshit01jan/codemie-repo.git
cd codemie-project

# Create virtual environment
python -m venv venv

# Activate virtual environment
# Windows:
venv\Scripts\activate
# Linux/Mac:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt
```

### 2. Configuration

```bash
# Create .env file
cp .env.example .env

# Edit .env with your settings
# Minimum required:
SECRET_KEY=your-development-secret-key
DEBUG=True
```

### 3. Database Initialization

```bash
# Initialize database with seed data
python scripts/init_db.py
```

### 4. Run Application

```bash
# Start the application
python run.py
```

Access at: `http://localhost:5000`

## Production Deployment

### Server Requirements

- **OS**: Ubuntu 20.04+ / CentOS 8+ / Windows Server 2019+
- **Python**: 3.12+
- **RAM**: Minimum 512MB, Recommended 2GB+
- **Storage**: 1GB minimum
- **Network**: HTTPS enabled (recommended)

### Deployment Steps

#### 1. Server Setup

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Python and dependencies
sudo apt install python3.12 python3.12-venv python3-pip nginx -y

# Create application user
sudo useradd -m -s /bin/bash appuser
```

#### 2. Application Deployment

```bash
# Switch to application user
sudo su - appuser

# Clone repository
git clone https://github.com/anshit01jan/codemie-repo.git
cd codemie-repo

# Create virtual environment
python3.12 -m venv venv
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Install production server
pip install gunicorn
```

#### 3. Environment Configuration

```bash
# Create production .env file
cat > .env << EOF
SECRET_KEY=$(python -c "import secrets; print(secrets.token_hex(32))")
DATABASE_PATH=/home/appuser/codemie-repo/database/login_app.db
PORT=8000
DEBUG=False
SESSION_COOKIE_SECURE=True
ENV=production
EOF

# Set proper permissions
chmod 600 .env
```

#### 4. Database Initialization

```bash
# Initialize database
python scripts/init_db.py

# Set proper permissions
chmod 644 database/login_app.db
```

#### 5. Create Systemd Service

```bash
# Exit to root user
exit

# Create service file
sudo nano /etc/systemd/system/login-app.service
```

Add the following content:

```ini
[Unit]
Description=Login Application
After=network.target

[Service]
Type=notify
User=appuser
Group=appuser
WorkingDirectory=/home/appuser/codemie-repo
Environment="PATH=/home/appuser/codemie-repo/venv/bin"
ExecStart=/home/appuser/codemie-repo/venv/bin/gunicorn \
    --workers 4 \
    --bind 127.0.0.1:8000 \
    --access-logfile /home/appuser/codemie-repo/logs/access.log \
    --error-logfile /home/appuser/codemie-repo/logs/error.log \
    --log-level info \
    "app:create_app()"
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable and start service
sudo systemctl enable login-app
sudo systemctl start login-app

# Check status
sudo systemctl status login-app
```

#### 6. Nginx Configuration

```bash
# Create Nginx configuration
sudo nano /etc/nginx/sites-available/login-app
```

Add the following content:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    # SSL Configuration
    ssl_certificate /etc/ssl/certs/your-cert.crt;
    ssl_certificate_key /etc/ssl/private/your-key.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Security Headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "no-referrer-when-downgrade" always;

    # Logs
    access_log /var/log/nginx/login-app-access.log;
    error_log /var/log/nginx/login-app-error.log;

    # Static files
    location /static {
        alias /home/appuser/codemie-repo/static;
        expires 30d;
        add_header Cache-Control "public, immutable";
    }

    # Application
    location / {
        proxy_pass http://127.0.0.1:8000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```

```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/login-app /etc/nginx/sites-enabled/

# Test configuration
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx
```

#### 7. SSL Certificate (Let's Encrypt)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtain certificate
sudo certbot --nginx -d your-domain.com

# Auto-renewal is set up automatically
# Test renewal
sudo certbot renew --dry-run
```

#### 8. Firewall Configuration

```bash
# Allow SSH, HTTP, HTTPS
sudo ufw allow OpenSSH
sudo ufw allow 'Nginx Full'

# Enable firewall
sudo ufw enable

# Check status
sudo ufw status
```

## Docker Deployment

### Dockerfile

Create `Dockerfile`:

```dockerfile
FROM python:3.12-slim

WORKDIR /app

# Install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt gunicorn

# Copy application
COPY . .

# Create logs directory
RUN mkdir -p logs

# Initialize database
RUN python scripts/init_db.py

# Expose port
EXPOSE 8000

# Run application
CMD ["gunicorn", "--workers", "4", "--bind", "0.0.0.0:8000", "app:create_app()"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  web:
    build: .
    ports:
      - "8000:8000"
    environment:
      - SECRET_KEY=${SECRET_KEY}
      - DATABASE_PATH=/app/database/login_app.db
      - PORT=8000
      - DEBUG=False
      - SESSION_COOKIE_SECURE=True
      - ENV=production
    volumes:
      - ./database:/app/database
      - ./logs:/app/logs
    restart: always
```

### Deploy with Docker

```bash
# Build and run
docker-compose up -d

# Check logs
docker-compose logs -f

# Stop
docker-compose down
```

## Monitoring & Maintenance

### Application Logs

```bash
# View application logs
tail -f logs/app.log

# View Gunicorn access logs
tail -f logs/access.log

# View Gunicorn error logs
tail -f logs/error.log
```

### System Monitoring

```bash
# Check application status
sudo systemctl status login-app

# Check Nginx status
sudo systemctl status nginx

# Check disk usage
df -h

# Check memory usage
free -h
```

### Log Rotation

Create `/etc/logrotate.d/login-app`:

```
/home/appuser/codemie-repo/logs/*.log {
    daily
    rotate 14
    compress
    delaycompress
    notifempty
    missingok
    create 0644 appuser appuser
    postrotate
        systemctl reload login-app
    endscript
}
```

### Database Backup

```bash
# Create backup script
cat > /home/appuser/backup-db.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/home/appuser/backups"
DB_PATH="/home/appuser/codemie-repo/database/login_app.db"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR
cp $DB_PATH "$BACKUP_DIR/login_app_$TIMESTAMP.db"

# Keep only last 7 days of backups
find $BACKUP_DIR -name "login_app_*.db" -mtime +7 -delete
EOF

chmod +x /home/appuser/backup-db.sh

# Add to crontab (daily at 2 AM)
crontab -e
# Add line:
# 0 2 * * * /home/appuser/backup-db.sh
```

## Updates & Maintenance

### Application Update

```bash
# Switch to application user
sudo su - appuser
cd codemie-repo

# Activate virtual environment
source venv/bin/activate

# Pull latest code
git pull origin main

# Install/update dependencies
pip install -r requirements.txt

# Run database migrations if any
# python scripts/migrate.py

# Exit to root
exit

# Restart application
sudo systemctl restart login-app

# Check status
sudo systemctl status login-app
```

### Security Updates

```bash
# System updates
sudo apt update && sudo apt upgrade -y

# Python package updates
pip list --outdated
pip install --upgrade <package-name>

# Regenerate requirements.txt
pip freeze > requirements.txt
```

## Troubleshooting

### Application Not Starting

```bash
# Check logs
sudo journalctl -u login-app -n 50 --no-pager

# Check application logs
tail -50 /home/appuser/codemie-repo/logs/error.log

# Verify environment variables
sudo su - appuser
cd codemie-repo
cat .env
```

### Database Issues

```bash
# Check database file
ls -lah database/login_app.db

# Reinitialize database
python scripts/init_db.py
```

### Nginx Issues

```bash
# Check Nginx configuration
sudo nginx -t

# Check Nginx logs
tail -f /var/log/nginx/login-app-error.log

# Restart Nginx
sudo systemctl restart nginx
```

### Permission Issues

```bash
# Fix application permissions
sudo chown -R appuser:appuser /home/appuser/codemie-repo

# Fix log permissions
sudo chmod -R 755 /home/appuser/codemie-repo/logs
sudo chmod 644 /home/appuser/codemie-repo/logs/*.log
```

## Performance Tuning

### Gunicorn Workers

Calculate optimal workers:
```
workers = (2 × CPU_cores) + 1
```

Adjust in service file:
```ini
--workers 4
```

### Database Optimization

```sql
-- Add indexes (already included in schema)
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Analyze database
ANALYZE;

-- Vacuum database (defragment)
VACUUM;
```

### Nginx Caching

Add to Nginx config:
```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=app_cache:10m max_size=100m;

location / {
    proxy_cache app_cache;
    proxy_cache_valid 200 10m;
    # ... rest of config
}
```

## Security Hardening

### System Hardening

```bash
# Disable root login
sudo sed -i 's/PermitRootLogin yes/PermitRootLogin no/' /etc/ssh/sshd_config
sudo systemctl restart sshd

# Install fail2ban
sudo apt install fail2ban -y
sudo systemctl enable fail2ban
sudo systemctl start fail2ban
```

### Application Security

1. ✅ Use strong SECRET_KEY
2. ✅ Enable HTTPS only (SESSION_COOKIE_SECURE=True)
3. ✅ Keep dependencies updated
4. ✅ Regular security audits
5. ✅ Monitor logs for suspicious activity
6. ✅ Database backups
7. ✅ Rate limiting enabled

## Rollback Procedure

```bash
# 1. Stop application
sudo systemctl stop login-app

# 2. Restore database backup
cd /home/appuser/codemie-repo
cp /home/appuser/backups/login_app_YYYYMMDD_HHMMSS.db database/login_app.db

# 3. Revert code
git checkout <previous-commit-hash>

# 4. Reinstall dependencies
source venv/bin/activate
pip install -r requirements.txt

# 5. Start application
sudo systemctl start login-app

# 6. Verify
sudo systemctl status login-app
```

## Support & Monitoring

### Health Check Endpoint

Add to application (future enhancement):
```python
@app.route('/health')
def health():
    return jsonify({'status': 'healthy'}), 200
```

### Uptime Monitoring

Set up external monitoring:
- UptimeRobot
- Pingdom
- StatusCake

### Alert Configuration

Configure alerts for:
- Application downtime
- High error rate
- Disk space low
- High memory usage
- SSL certificate expiry

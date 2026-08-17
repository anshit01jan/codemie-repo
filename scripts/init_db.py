import sqlite3
import os
from werkzeug.security import generate_password_hash
from datetime import datetime

def init_database():
    db_dir = 'database'
    db_path = os.path.join(db_dir, 'login_app.db')

    if not os.path.exists(db_dir):
        os.makedirs(db_dir)

    if os.path.exists(db_path):
        os.remove(db_path)
        print(f'Removed existing database: {db_path}')

    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    print('Creating users table...')
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username VARCHAR(50) NOT NULL UNIQUE,
            email VARCHAR(100) NOT NULL UNIQUE,
            password_hash VARCHAR(255) NOT NULL,
            failed_attempts INTEGER DEFAULT 0,
            locked_until TEXT,
            created_at TEXT NOT NULL,
            updated_at TEXT NOT NULL
        )
    ''')

    cursor.execute('CREATE INDEX idx_users_username ON users(username)')
    cursor.execute('CREATE INDEX idx_users_email ON users(email)')
    cursor.execute('CREATE INDEX idx_users_locked_until ON users(locked_until)')

    print('Seeding users table...')
    password_hash = generate_password_hash('ScrumPass1')
    now = datetime.utcnow().isoformat()

    cursor.execute('''
        INSERT INTO users (username, email, password_hash, failed_attempts, locked_until, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    ''', ('scrum50', 'scrum50@example.com', password_hash, 0, None, now, now))

    conn.commit()
    conn.close()

    print(f'Database initialized successfully at: {db_path}')
    print('Default user created:')
    print('  Username: scrum50')
    print('  Password: ScrumPass1')
    print('  Email: scrum50@example.com')

if __name__ == '__main__':
    init_database()

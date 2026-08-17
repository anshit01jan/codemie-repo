import sqlite3
from datetime import datetime
from app.models.user import User

class UserRepository:
    def __init__(self, db_path):
        self.db_path = db_path

    def _get_connection(self):
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        return conn

    def find_by_username(self, username):
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                'SELECT * FROM users WHERE username = ?',
                (username,)
            )
            row = cursor.fetchone()
            if row:
                return User(
                    id=row['id'],
                    username=row['username'],
                    email=row['email'],
                    password_hash=row['password_hash'],
                    failed_attempts=row['failed_attempts'],
                    locked_until=datetime.fromisoformat(row['locked_until']) if row['locked_until'] else None,
                    created_at=datetime.fromisoformat(row['created_at']) if row['created_at'] else None,
                    updated_at=datetime.fromisoformat(row['updated_at']) if row['updated_at'] else None
                )
            return None
        finally:
            conn.close()

    def find_by_email(self, email):
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                'SELECT * FROM users WHERE email = ?',
                (email,)
            )
            row = cursor.fetchone()
            if row:
                return User(
                    id=row['id'],
                    username=row['username'],
                    email=row['email'],
                    password_hash=row['password_hash'],
                    failed_attempts=row['failed_attempts'],
                    locked_until=datetime.fromisoformat(row['locked_until']) if row['locked_until'] else None,
                    created_at=datetime.fromisoformat(row['created_at']) if row['created_at'] else None,
                    updated_at=datetime.fromisoformat(row['updated_at']) if row['updated_at'] else None
                )
            return None
        finally:
            conn.close()

    def update_failed_attempts(self, username, failed_attempts, locked_until=None):
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                'UPDATE users SET failed_attempts = ?, locked_until = ?, updated_at = ? WHERE username = ?',
                (failed_attempts, locked_until.isoformat() if locked_until else None, datetime.utcnow().isoformat(), username)
            )
            conn.commit()
        finally:
            conn.close()

    def reset_failed_attempts(self, username):
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                'UPDATE users SET failed_attempts = 0, locked_until = NULL, updated_at = ? WHERE username = ?',
                (datetime.utcnow().isoformat(), username)
            )
            conn.commit()
        finally:
            conn.close()

    def create(self, user):
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                '''INSERT INTO users (username, email, password_hash, failed_attempts, locked_until, created_at, updated_at)
                   VALUES (?, ?, ?, ?, ?, ?, ?)''',
                (user.username, user.email, user.password_hash, user.failed_attempts,
                 user.locked_until.isoformat() if user.locked_until else None,
                 user.created_at.isoformat(), user.updated_at.isoformat())
            )
            conn.commit()
            return cursor.lastrowid
        finally:
            conn.close()

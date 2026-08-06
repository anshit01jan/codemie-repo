from datetime import datetime

class User:
    def __init__(self, id, username, email, password_hash, failed_attempts=0,
                 locked_until=None, created_at=None, updated_at=None):
        self.id = id
        self.username = username
        self.email = email
        self.password_hash = password_hash
        self.failed_attempts = failed_attempts
        self.locked_until = locked_until
        self.created_at = created_at or datetime.utcnow()
        self.updated_at = updated_at or datetime.utcnow()

    def to_dict(self):
        return {
            'id': self.id,
            'username': self.username,
            'email': self.email,
            'failed_attempts': self.failed_attempts,
            'locked_until': self.locked_until.isoformat() if self.locked_until else None,
            'created_at': self.created_at.isoformat() if self.created_at else None,
            'updated_at': self.updated_at.isoformat() if self.updated_at else None
        }

    def is_locked(self):
        if self.locked_until is None:
            return False
        return datetime.utcnow() < self.locked_until

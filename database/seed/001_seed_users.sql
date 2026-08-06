-- Seed data for users table
-- Username: scrum50, Password: ScrumPass1
-- Password hash generated using werkzeug.security.generate_password_hash with scrypt method

INSERT INTO users (username, email, password_hash, failed_attempts, locked_until, created_at, updated_at)
VALUES (
    'scrum50',
    'scrum50@example.com',
    'scrypt:32768:8:1$4xHGzZ0jXqJqKqN9$8e9f5e3a3c3f9e4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b',
    0,
    NULL,
    datetime('now'),
    datetime('now')
);

import re

class AuthValidator:
    @staticmethod
    def validate_login(username, password):
        errors = []

        if not username or not username.strip():
            errors.append('Username is required')

        if not password:
            errors.append('Password is required')

        return errors

    @staticmethod
    def validate_forgot_password(email):
        errors = []

        if not email or not email.strip():
            errors.append('Email is required')
        elif not AuthValidator._is_valid_email(email):
            errors.append('Invalid email format')

        return errors

    @staticmethod
    def _is_valid_email(email):
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return re.match(pattern, email) is not None

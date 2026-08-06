from datetime import datetime, timedelta
from werkzeug.security import check_password_hash
from app.repositories.user_repository import UserRepository
from app.validators.auth_validator import AuthValidator
import logging

logger = logging.getLogger(__name__)

class AuthService:
    MAX_FAILED_ATTEMPTS = 3
    LOCKOUT_DURATION_SECONDS = 10

    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def authenticate(self, username, password):
        validation_errors = AuthValidator.validate_login(username, password)
        if validation_errors:
            return {'success': False, 'errors': validation_errors}

        user = self.user_repository.find_by_username(username)

        if not user:
            logger.warning(f'Login attempt with non-existent username: {username}')
            return {'success': False, 'errors': ['Invalid username or password']}

        if user.is_locked():
            remaining_seconds = int((user.locked_until - datetime.utcnow()).total_seconds())
            logger.warning(f'Login attempt on locked account: {username}')
            return {
                'success': False,
                'errors': [f'Account is locked. Please try after {remaining_seconds} seconds.'],
                'locked': True
            }

        if not check_password_hash(user.password_hash, password):
            failed_attempts = user.failed_attempts + 1
            logger.warning(f'Failed login attempt for user: {username} (attempt {failed_attempts})')

            if failed_attempts >= self.MAX_FAILED_ATTEMPTS:
                locked_until = datetime.utcnow() + timedelta(seconds=self.LOCKOUT_DURATION_SECONDS)
                self.user_repository.update_failed_attempts(username, failed_attempts, locked_until)
                logger.warning(f'Account locked for user: {username} until {locked_until}')
                return {
                    'success': False,
                    'errors': [f'Account is locked. Please try after {self.LOCKOUT_DURATION_SECONDS} seconds.'],
                    'locked': True
                }
            else:
                self.user_repository.update_failed_attempts(username, failed_attempts)
                remaining_attempts = self.MAX_FAILED_ATTEMPTS - failed_attempts
                return {
                    'success': False,
                    'errors': [f'Invalid username or password. {remaining_attempts} attempt(s) remaining.']
                }

        self.user_repository.reset_failed_attempts(username)
        logger.info(f'Successful login for user: {username}')

        return {
            'success': True,
            'user': user.to_dict(),
            'message': 'Login successful'
        }

    def forgot_password(self, email):
        validation_errors = AuthValidator.validate_forgot_password(email)
        if validation_errors:
            return {'success': False, 'errors': validation_errors}

        user = self.user_repository.find_by_email(email)

        if user:
            logger.info(f'Forgot password request for registered email: {email}')
        else:
            logger.warning(f'Forgot password request for non-registered email: {email}')

        return {
            'success': True,
            'message': 'Forgot Password link has been sent to registered email address.'
        }

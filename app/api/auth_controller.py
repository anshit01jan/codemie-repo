from flask import Blueprint, request, jsonify, render_template, session
from app.services.auth_service import AuthService
from app.repositories.user_repository import UserRepository
from app.middleware.rate_limiter import rate_limiter
import os
import logging

logger = logging.getLogger(__name__)

auth_bp = Blueprint('auth', __name__)

db_path = os.getenv('DATABASE_PATH', 'database/login_app.db')
user_repository = UserRepository(db_path)
auth_service = AuthService(user_repository)

@auth_bp.route('/')
def index():
    return render_template('login.html')

@auth_bp.route('/api/login', methods=['POST'])
@rate_limiter.limit(max_requests=10, window_seconds=60)
def login():
    try:
        data = request.get_json()

        if not data:
            return jsonify({'success': False, 'errors': ['Invalid request data']}), 400

        username = data.get('username', '').strip()
        password = data.get('password', '')

        result = auth_service.authenticate(username, password)

        if result['success']:
            session['user_id'] = result['user']['id']
            session['username'] = result['user']['username']
            logger.info(f'User logged in successfully: {username}')
            return jsonify(result), 200
        else:
            status_code = 423 if result.get('locked') else 401
            return jsonify(result), status_code

    except Exception as e:
        logger.error(f'Login error: {str(e)}', exc_info=True)
        return jsonify({
            'success': False,
            'errors': ['An error occurred during login. Please try again.']
        }), 500

@auth_bp.route('/api/forgot-password', methods=['POST'])
@rate_limiter.limit(max_requests=5, window_seconds=60)
def forgot_password():
    try:
        data = request.get_json()

        if not data:
            return jsonify({'success': False, 'errors': ['Invalid request data']}), 400

        email = data.get('email', '').strip()

        result = auth_service.forgot_password(email)

        if result['success']:
            return jsonify(result), 200
        else:
            return jsonify(result), 400

    except Exception as e:
        logger.error(f'Forgot password error: {str(e)}', exc_info=True)
        return jsonify({
            'success': False,
            'errors': ['An error occurred. Please try again.']
        }), 500

@auth_bp.route('/api/logout', methods=['POST'])
def logout():
    username = session.get('username')
    session.clear()
    logger.info(f'User logged out: {username}')
    return jsonify({'success': True, 'message': 'Logged out successfully'}), 200

@auth_bp.route('/dashboard')
def dashboard():
    if 'username' not in session:
        return render_template('login.html')
    return render_template('dashboard.html', username=session['username'])

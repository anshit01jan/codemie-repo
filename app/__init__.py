from flask import Flask
from flask_wtf.csrf import CSRFProtect
import os
import logging

csrf = CSRFProtect()

def create_app():
    app = Flask(__name__,
                template_folder='../templates',
                static_folder='../static')

    secret_key = os.getenv('SECRET_KEY')
    env = os.getenv('ENV', 'development')
    if not secret_key:
        if env == 'production':
            raise RuntimeError('SECRET_KEY environment variable must be set in production')
        secret_key = 'dev-secret-key-change-in-production'
        logging.warning('Using default SECRET_KEY. Set SECRET_KEY environment variable in production.')

    app.config['SECRET_KEY'] = secret_key
    app.config['SESSION_COOKIE_SECURE'] = os.getenv('SESSION_COOKIE_SECURE', 'False').lower() == 'true'
    app.config['SESSION_COOKIE_HTTPONLY'] = True
    app.config['SESSION_COOKIE_SAMESITE'] = 'Lax'
    app.config['PERMANENT_SESSION_LIFETIME'] = 1800

    csrf.init_app(app)

    from app.api.auth_controller import auth_bp
    app.register_blueprint(auth_bp)

    logs_dir = 'logs'
    if not os.path.exists(logs_dir):
        os.makedirs(logs_dir)

    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        handlers=[
            logging.FileHandler('logs/app.log'),
            logging.StreamHandler()
        ]
    )

    return app

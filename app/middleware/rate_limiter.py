from functools import wraps
from flask import request, jsonify
from datetime import datetime, timedelta
import logging

logger = logging.getLogger(__name__)

class RateLimiter:
    def __init__(self):
        self.attempts = {}

    def limit(self, max_requests=5, window_seconds=60):
        def decorator(f):
            @wraps(f)
            def decorated_function(*args, **kwargs):
                client_ip = request.remote_addr
                now = datetime.utcnow()

                if client_ip not in self.attempts:
                    self.attempts[client_ip] = []

                self.attempts[client_ip] = [
                    timestamp for timestamp in self.attempts[client_ip]
                    if now - timestamp < timedelta(seconds=window_seconds)
                ]

                if len(self.attempts[client_ip]) >= max_requests:
                    logger.warning(f'Rate limit exceeded for IP: {client_ip}')
                    return jsonify({
                        'success': False,
                        'errors': ['Too many requests. Please try again later.']
                    }), 429

                self.attempts[client_ip].append(now)
                return f(*args, **kwargs)

            return decorated_function
        return decorator

rate_limiter = RateLimiter()

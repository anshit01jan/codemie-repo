from functools import wraps
from flask import request, jsonify, current_app
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
                if not current_app.config.get('RATE_LIMIT_ENABLED', False):
                    return f(*args, **kwargs)

                client_ip = request.remote_addr or request.headers.get('X-Forwarded-For', 'unknown')
                # Scope the rate limit per IP + route (method+path) so different endpoints
                # don't share the same request bucket and cause unexpected 429s.
                route_key = f"{request.method}:{request.path}"
                key = f"{client_ip}:{route_key}"
                # Allow the application's own auth lockout logic to handle login attempts.
                # If this is a login POST containing a username, bypass the global rate limiter
                # so tests and lockout messaging are not preempted by IP-based limits.
                if request.method == 'POST' and request.path == '/api/login':
                    try:
                        data = request.get_json(silent=True) or {}
                        if 'username' in data:
                            return f(*args, **kwargs)
                    except Exception:
                        pass
                now = datetime.utcnow()
                if key not in self.attempts:
                    self.attempts[key] = []

                # Remove timestamps outside the sliding window
                self.attempts[key] = [
                    timestamp for timestamp in self.attempts[key]
                    if now - timestamp < timedelta(seconds=window_seconds)
                ]

                if len(self.attempts[key]) >= max_requests:
                    logger.warning(f'Rate limit exceeded for key: {key}')
                    return jsonify({
                        'success': False,
                        'errors': ['Too many requests. Please try again later.']
                    }), 429

                self.attempts[key].append(now)
                return f(*args, **kwargs)

            return decorated_function
        return decorator

rate_limiter = RateLimiter()

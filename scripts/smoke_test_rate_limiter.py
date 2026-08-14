import os
import json
from app import create_app

# Enable rate limiter for the test
os.environ['RATE_LIMIT_ENABLED'] = 'True'
app = create_app()

client = app.test_client()

login_payload = {'username': 'test', 'password': 'x'}
forgot_payload = {'email': 'x@x.com'}

print('Sending 6 login requests...')
for i in range(6):
    resp = client.post('/api/login', data=json.dumps(login_payload), content_type='application/json')
    print(i+1, resp.status_code, resp.get_data(as_text=True)[:120])

print('\nSending 1 forgot-password request...')
resp = client.post('/api/forgot-password', data=json.dumps(forgot_payload), content_type='application/json')
print('forgot-password:', resp.status_code, resp.get_data(as_text=True)[:200])

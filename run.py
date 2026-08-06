import os
from app import create_app

if __name__ == '__main__':
    logs_dir = 'logs'
    if not os.path.exists(logs_dir):
        os.makedirs(logs_dir)

    app = create_app()

    port = int(os.getenv('PORT', 5000))
    debug = os.getenv('DEBUG', 'False').lower() == 'true'

    print('='*60)
    print(f'Starting Login Application')
    print(f'Environment: {app.config["ENV"]}')
    print(f'Running on: http://localhost:{port}')
    print(f'Debug mode: {debug}')
    print('='*60)

    app.run(host='0.0.0.0', port=port, debug=debug)

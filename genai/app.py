import os
from dotenv import load_dotenv
from waitress import serve
from flask import Flask
from controller.matching_controller import matching_bp

app = Flask(__name__)
app.register_blueprint(matching_bp)

load_dotenv()

if __name__ == '__main__':
    use_waitress = os.getenv("USE_WAITRESS", "false").lower() == "true"
    port = int(os.getenv("PORT", 8000))
    if use_waitress:
        serve(app, host='0.0.0.0', port=port)
    else:
        app.run(host='0.0.0.0', port=port, debug=True)

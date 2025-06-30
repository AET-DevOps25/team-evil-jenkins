"""Central configuration for the GenAI service.
Loads environment variables (optionally via a .env file when run locally)
that are required to talk to the Open WebUI LLM backend.

We deliberately fail fast if mandatory vars are missing so that container
start-up issues are obvious in logs.
"""
from __future__ import annotations

import os
from dotenv import load_dotenv

# Load variables from local .env file when running outside Docker.
load_dotenv()

OPENWEBUI_URL: str | None = os.getenv("OPENWEBUI_URL")
OPENWEBUI_API_KEY: str | None = os.getenv("OPENWEBUI_API_KEY")

if not OPENWEBUI_URL or not OPENWEBUI_API_KEY:
    raise EnvironmentError(
        "OPENWEBUI_URL and OPENWEBUI_API_KEY environment variables must be set!"
    )

# Optional tuning parameters
REQUEST_TIMEOUT_SECONDS: int = int(os.getenv("OPENWEBUI_TIMEOUT", 60))

TOP_K_MATCHES: int = int(os.getenv("GENAI_TOP_K", 10))

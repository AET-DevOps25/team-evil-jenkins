# GenAI Microservice

This service provides a REST API for AI-powered user matching using an LLM (via Open WebUI API).

## Features
- **/genai/match** endpoint: Given a user profile and a list of candidate users, returns the top-matching candidate IDs using an LLM.
- Pure FastAPI implementation, ready for containerized deployment.

## Usage

### 1. Environment Variables
You must supply the following variables (either in a `.env` file or via your orchestrator):

- `OPENWEBUI_URL` – Base URL of your Open WebUI instance (e.g. `https://gpu.aet.cit.tum.de`)
- `OPENWEBUI_API_KEY` – Bearer token for Open WebUI API access
- `PORT` (optional, default `8000`)

**Recommended:**
- Define these in a `.env` file in the **project root** (the same directory as `docker-compose.yml`). Docker Compose will automatically load them for all services.
- Optionally, you may also create a `genai/.env.example` to document required variables for local development.

### 2. Build & Run (Docker Compose)

```sh
docker compose up --build genai
```

### 3. API Example

```http
POST /genai/match
Content-Type: application/json

{
  "user_profile": "Sporty person who likes tennis",
  "candidates": [
    {"id": "u1", "profile": "Tennis fan"},
    {"id": "u2", "profile": "Chess lover"}
  ]
}
```

Response:
```json
{"ranked_ids": ["u1", "u2"]}
```

## Development
- Main entrypoint: `app.py` (FastAPI)
- API routes: `routes/matching.py`
- LLM logic: `openwebui_client.py`, `matching_engine.py`

## Notes
- All secrets (API keys) should be kept out of version control. Only commit `.env.example`.
- For more Open WebUI API info, see: https://docs.openwebui.com/getting-started/api-endpoints/

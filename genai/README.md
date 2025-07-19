# GenAI Microservice

This service provides a REST API for AI-powered user matching using an LLM (via Open WebUI API).

## Features
- **/genai/match** endpoint: Given a user profile and a list of candidate users, returns a ranked list of structured match objects (with id, score, explanation, and common preferences) using an LLM.
- Pure FastAPI implementation, ready for containerized deployment.

## Usage

### 1. Environment Variables
You must supply the following variables (either in a `.env` file or via your orchestrator):

- `OPENWEBUI_URL` – Base URL of your Open WebUI instance (e.g. `https://gpu.aet.cit.tum.de`)
- `OPENWEBUI_API_KEY` – Bearer token for Open WebUI API access


**Recommended:**
- Define these in a `.env` file in the **project root** (the same directory as `docker-compose.yml`). Docker Compose will automatically load them for all services.


### 2. Build & Run (Docker Compose)

```sh
docker compose up --build genai
```

### 3. API Example

#### POST /genai/match
Request body:
```json
{
  "user_profile": {"id": "user1", "name": "user", "sportInterests": ["Tennis", "Swimming"], "bio": "I love tennis and swimming", "skillLevel": "Beginner"},
  "candidates": [
    {"id": "u1", "name": "Bob", "sportInterests": ["Tennis", "Swimming"], "bio": "I love tennis and swimming", "skillLevel": "Beginner"},
    {"id": "u2", "name": "Carol", "sportInterests": ["Chess", "Reading"], "bio": "I love chess and reading", "skillLevel": "Beginner"}
  ]
}
```
Response:
```json
{
  "matches": [
    {
      "id": "u1",
      "score": 0.92,
      "explanation": "Both enjoy tennis",
      "common_preferences": ["Tennis"]
    },
    {
      "id": "u2",
      "score": 0.4,
      "explanation": "No common sports",
      "common_preferences": []
    }
  ]
}
```

### 4. Testing
- See `tests/test_matching.py` for endpoint tests. These check that the `/genai/match` endpoint returns the correct schema (list of match objects with `id`, `score`, `explanation`, `common_preferences`).
- The output of the LLM is not strictly deterministic; tests validate response structure and types, not exact values.

### 5. Internal Logic
- Matching logic is implemented in `matching_engine.py`, which calls `openwebui_client.py` to interact with the LLM API.
- The LLM is prompted to return only a strict JSON array of match objects.
- All parsing and error handling is defensive to handle LLM quirks.

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

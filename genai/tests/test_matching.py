from fastapi.testclient import TestClient
from genai.app import app

client = TestClient(app)

def test_match_endpoint_returns_ranked_ids():
    payload = {
        "user_profile": "I love tennis and hiking.",
        "candidates": [
            {"id": "u1", "profile": "Enjoys tennis and swimming."},
            {"id": "u2", "profile": "Fan of chess and reading."}
        ]
    }
    response = client.post("/genai/match", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "ranked_ids" in data
    assert isinstance(data["ranked_ids"], list)

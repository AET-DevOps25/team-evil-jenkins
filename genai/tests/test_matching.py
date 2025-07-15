from fastapi.testclient import TestClient
from genai.app import app
import pytest

client = TestClient(app)

@pytest.fixture(autouse=True)
def patch_rank_candidates(monkeypatch):
    def fake_rank_candidates(user, candidates, top_k=None):
        return [
            {
                "id": "u1",
                "score": 0.92,
                "explanation": "Both enjoy tennis",
                "common_preferences": ["Tennis"]
            }
        ]
    import genai.matching_engine
    monkeypatch.setattr(genai.matching_engine.openwebui_client, "rank_candidates", fake_rank_candidates)

def test_match_endpoint_returns_matches():
    payload = {
        "user": {"id": "u0", "name": "Alice", "sportInterests": ["Tennis", "Hiking"]},
        "candidates": [
            {"id": "u1", "name": "Bob", "sportInterests": ["Tennis", "Swimming"]},
            {"id": "u2", "name": "Carol", "sportInterests": ["Chess", "Reading"]}
        ]
    }
    response = client.post("/genai/match", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "matches" in data
    assert isinstance(data["matches"], list)
    if data["matches"]:
        match = data["matches"][0]
        assert "id" in match
        assert "score" in match
        assert "explanation" in match
        assert "common_preferences" in match
        assert isinstance(match["common_preferences"], list)

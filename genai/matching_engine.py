"""Engine that uses Open WebUI client to rank candidate users."""
from __future__ import annotations

from typing import List, Dict, Any

import openwebui_client


class MatchingEngine:
    def __init__(self):
        pass

    def match(self, user: dict, candidates: List[dict]) -> List[dict]:
        """Return ordered list of match objects using OpenWebUI (user and candidates as dicts)."""
        # All candidates and user are dicts with id, name, sportInterests
        return openwebui_client.rank_candidates(user, candidates)


if __name__ == "__main__":
    # Example usage
    engine = MatchingEngine()
    user = {"id": "u1", "name": "Alice", "sportInterests": ["Tennis", "Hiking"]}
    candidates = [
        {"id": "u2", "name": "Bob", "sportInterests": ["Tennis", "Swimming"]},
        {"id": "u3", "name": "Carol", "sportInterests": ["Chess", "Reading"]}
    ]
    scores = engine.match(user, candidates)
    print("Matching scores:", scores)

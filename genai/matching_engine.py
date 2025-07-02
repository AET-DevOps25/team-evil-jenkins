"""Engine that uses Open WebUI client to rank candidate users."""
from __future__ import annotations

from typing import List, Dict

import openwebui_client


class MatchingEngine:
    def __init__(self):
        pass

    def match(self, user_profile: str, candidates: List[Dict]) -> List[str]:
        """Return ordered list of candidate IDs representing best matches."""
        return openwebui_client.rank_candidates(user_profile, candidates)

if __name__ == "__main__":
    # Example usage
    engine = MatchingEngine()
    user = "Sporty person who likes tennis"
    candidates = ["Football fan", "Tennis enthusiast", "Casual runner"]
    scores = engine.match(user, candidates)
    print("Matching scores:", scores)

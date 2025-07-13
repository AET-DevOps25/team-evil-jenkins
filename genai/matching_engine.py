"""Engine that uses Open WebUI client to rank candidate users."""
from __future__ import annotations

from typing import List, Dict

import openwebui_client


class MatchingEngine:
    def __init__(self):
        pass

    def match(self, user_profile: str, candidates: List[Dict]) -> List[str]:
        """Return ordered list of candidate IDs representing best matches, preferring those with known location."""
        # Prefer candidates with known location, then by profile (fallback to OpenWebUI)
        with_location = [c for c in candidates if c.get('location') and c['location'] != 'unknown']
        without_location = [c for c in candidates if not c.get('location') or c['location'] == 'unknown']
        # For demo: just concatenate, real logic can use location distance
        ordered = with_location + without_location
        # fallback to OpenWebUI if needed
        if not ordered:
            return openwebui_client.rank_candidates(user_profile, candidates)
        return [c['id'] for c in ordered]

if __name__ == "__main__":
    # Example usage
    engine = MatchingEngine()
    user = "Sporty person who likes tennis"
    candidates = ["Football fan", "Tennis enthusiast", "Casual runner"]
    scores = engine.match(user, candidates)
    print("Matching scores:", scores)

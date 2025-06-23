import os
import numpy as np
from typing import List

class MatchingEngine:
    def __init__(self):
        # Example: Load any necessary models or embeddings here
        pass

    def embed(self, data: str) -> np.ndarray:
        # Dummy embedding: replace with real embedding logic (e.g., from transformers, sentence-transformers, etc.)
        return np.random.rand(128)

    def calculate_similarity(self, emb1: np.ndarray, emb2: np.ndarray) -> float:
        # Cosine similarity
        if np.linalg.norm(emb1) == 0 or np.linalg.norm(emb2) == 0:
            return 0.0
        return float(np.dot(emb1, emb2) / (np.linalg.norm(emb1) * np.linalg.norm(emb2)))

    def match(self, user_profile: str, candidates: List[str]) -> List[float]:
        user_emb = self.embed(user_profile)
        return [self.calculate_similarity(user_emb, self.embed(cand)) for cand in candidates]

if __name__ == "__main__":
    # Example usage
    engine = MatchingEngine()
    user = "Sporty person who likes tennis"
    candidates = ["Football fan", "Tennis enthusiast", "Casual runner"]
    scores = engine.match(user, candidates)
    print("Matching scores:", scores)

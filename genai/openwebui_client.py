"""Lightweight client for interacting with an Open WebUI OpenAI-compatible endpoint.
The endpoint URL and API key are read from config.py .

Currently it only exposes a `chat_completion` helper tailored for the matching
use-case, but you can easily extend it for other models/methods.
"""
from __future__ import annotations

import json
from typing import List
import requests

import config

HEADERS = {
    "Authorization": f"Bearer {config.OPENWEBUI_API_KEY}",
    "Content-Type": "application/json",
}

ENDPOINT = config.OPENWEBUI_URL.rstrip("/") + "/api/chat/completions"


def rank_candidates(user: dict, candidates: List[dict], top_k: int | None = None) -> List[dict]:
    """Ask the LLM to rank `candidates` for the active user (as a structured dict).

    Parameters
    ----------
    user : dict
        The active user as a structured candidate dict (id, name, sportInterests).
    candidates : List[dict]
        List of candidate user dictionaries. Each dict MUST contain at least an
        `id` field, `name`, and `sportInterests`.
    top_k : Optional[int]
        Limit the number of ids returned. Defaults to config.TOP_K_MATCHES.

    Returns
    -------
    List[dict]
        Ordered list of match objects (id, score, explanation, common_preferences).
    """
    if top_k is None:
        top_k = config.TOP_K_MATCHES

    # Instruct the model VERY CLEARLY to respond with **only** a JSON array of objects.
    # Each object MUST contain: id (string), score (number 0-1), explanation (string), common_preferences (array of strings).
    system_prompt = (
        "You are a matchmaking engine.  Given one active user (as a JSON object) and a set of candidate users, "
        "rank the candidates by compatibility and provide additional details.  **Respond ONLY with a JSON "
        "array where each element is an object with the following keys (exact names): id (string), score (number between 0 and 1), "
        "explanation (string), common_preferences (array of strings).  Do NOT wrap the JSON in Markdown fences, do NOT add "
        "extra keys or commentary.  The response MUST look like this (example):\n"
        '[{"id": "u17", "score": 0.92, "explanation": "Both play tennis regularly", "common_preferences": ["Tennis"]}, '
        '{"id": "u42", "score": 0.88, "explanation": "Enjoy hiking", "common_preferences": ["Hiking"]} ]'
    )

    user_prompt = (
        "Active user (JSON object):\n" + json.dumps(user, ensure_ascii=False) + "\n\n" +
        "Candidates (JSON, each with id, name, sportInterests):\n" + json.dumps(candidates, ensure_ascii=False)
    )

    payload = {
        "model": "llama3.3:latest",  # Open WebUI supports this alias
        "messages": [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt},
        ],
        "temperature": 0.2,
    }

    resp = requests.post(ENDPOINT, json=payload, headers=HEADERS, timeout=config.REQUEST_TIMEOUT_SECONDS)
    resp.raise_for_status()

    content = resp.json()["choices"][0]["message"]["content"].strip()
    # Expect pure JSON list.  If the model wrapped it in markdown, try to strip.
    # The model has been instructed to output ONLY a JSON array.  No fences expected.
    print("RAW LLM content:", content[:400])
    try:
        matches = json.loads(content)
        return matches[:top_k]
    except json.JSONDecodeError as exc:
        # Surface the error – downstream service should respond 500 so we notice.
        raise ValueError(f"GenAI did not return strict JSON array: {content}") from exc

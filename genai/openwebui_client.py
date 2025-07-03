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


def rank_candidates(user_profile: str, candidates: List[dict], top_k: int | None = None) -> List[str]:
    """Ask the LLM to rank `candidates` for `user_profile`.

    Parameters
    ----------
    user_profile : str
        Natural-language description of the active user.
    candidates : List[dict]
        List of candidate user dictionaries. Each dict MUST contain at least an
        `id` field and a free-text `profile` field.
    top_k : Optional[int]
        Limit the number of ids returned. Defaults to config.TOP_K_MATCHES.

    Returns
    -------
    List[str]
        Ordered list of candidate ids (best first).
    """
    if top_k is None:
        top_k = config.TOP_K_MATCHES

    system_prompt = (
        "You are a matchmaking engine.  Given one user and a set of other user\n"
        "profiles, return the IDs of the most compatible candidates ordered by\n"
        "compatibility (best first) as a JSON array of strings, no extra text."
    )

    user_prompt = (
        "Active user:\n" + user_profile + "\n\n" +
        "Candidates (JSON):\n" + json.dumps(candidates, ensure_ascii=False)
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
    if content.startswith("```"):
        content = content.split("```", 2)[1]
    try:
        return json.loads(content)[:top_k]
    except json.JSONDecodeError:
        # Fallback: return empty list to avoid crashing the service.
        return []

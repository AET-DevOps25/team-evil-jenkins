"""FastAPI router exposing matchmaking endpoint for other services."""
from __future__ import annotations

from typing import List, Dict

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field

from matching_engine import MatchingEngine

router = APIRouter(prefix="/genai", tags=["GenAI Matching"])
engine = MatchingEngine()


class Candidate(BaseModel):
    id: str = Field(..., description="Unique ID of the candidate user")
    profile: str = Field(..., description="Natural-language profile description")


class MatchRequest(BaseModel):
    user_profile: str = Field(..., description="Active user profile in natural language")
    candidates: List[Candidate] = Field(..., description="List of candidate users to rank")


class MatchResponse(BaseModel):
    ranked_ids: List[str] = Field(..., description="IDs of best candidates, ordered by compatibility")


@router.post("/match", response_model=MatchResponse)
async def match(req: MatchRequest) -> MatchResponse:  # noqa: D401  (simple func)
    if not req.user_profile.strip():
        raise HTTPException(status_code=400, detail="user_profile cannot be empty")
    if not req.candidates:
        raise HTTPException(status_code=400, detail="candidates list cannot be empty")

    # Convert Candidate models to dicts
    candidates_data: List[Dict] = [c.dict() for c in req.candidates]

    try:
        ranked = engine.match(req.user_profile, candidates_data)
        return MatchResponse(ranked_ids=ranked)
    except Exception as exc:  # noqa: BLE001
        raise HTTPException(status_code=500, detail=f"Matching failed: {exc}") from exc
@router.get("/health")
def health():
    return {"status": "ok"}
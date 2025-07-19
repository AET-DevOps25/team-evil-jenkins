"""FastAPI application entry point for the GenAI service."""
from __future__ import annotations

import os
from dotenv import load_dotenv
from fastapi import FastAPI

from routes.matching import router as matching_router
from prometheus_fastapi_instrumentator import Instrumentator

load_dotenv()

app = FastAPI(title="GenAI Service")
app.include_router(matching_router)

Instrumentator().instrument(app).expose(app)

@app.on_event("startup")
async def startup():
    """Instrument the app with Prometheus metrics."""

@app.get("/health")
async def health():
    """Simple health check endpoint for Kubernetes probes."""
    return {"status": "ok"}

# Local dev convenience
if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=int(os.getenv("PORT", 8000)))

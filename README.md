# Team Evil Jenkins

A platform to connect people for outdoor sports, featuring smart matching (GenAI), messaging, and seamless deployment via Docker, Kubernetes, and AWS.

---

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Service Table](#service-table)
- [Local Development](#local-development)
- [Kubernetes & Cloud Deployment](#kubernetes--cloud-deployment)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The core functionality of the application is to connect individuals with shared interests in outdoor sports and physical activities. Users create profiles, specify their preferred sports and availability, and discover potential partners or groups in their area. The platform offers recommendations, matching, and communication tools to reduce the time and effort required to find activity partners.

### Intended Users
- Amateur outdoor sport enthusiasts
- Newcomers to a city
- Busy professionals
- Sports groups or event organizers

### GenAI Integration
GenAI enhances user matching by analyzing profiles in a context-aware manner. Instead of relying solely on filters like location or shared interests, GenAI predicts compatibility using LLM-based analysis.

### Example Scenarios
- **New to City:** Lena moves to Munich and wants to find a hiking group. She signs up, selects "hiking," adds her availability, and finds matches.
- **Busy Professional:** Max wants to stay active but has limited time. He fills out his sports interests and availability, and the platform suggests partners.
- **Marathon Prep:** Ali is training for a marathon and wants running partners for early mornings. He enters his preferences and finds suitable matches.

---

## Architecture

The system is built as a set of microservices:

| Service            | Technology Stack        | Port | Description                              |
|--------------------|------------------------|------|------------------------------------------|
| client             | React + NGINX          | 3000 | Frontend web app                         |
| user-service       | Spring Boot (Java)     | 8080 | User profiles, auth, matching            |
| location-service   | Spring Boot (Java)     | 8081 | Location data, geospatial logic          |
| genai              | FastAPI (Python)       | 8000 | AI/LLM matching, embeddings              |
| messaging-service  | Spring Boot (Java)     | 8082 | Messaging, WebSocket                     |
| api-gateway        | NGINX + Lua            | 80   | API gateway, JWT, CORS, proxy            |
| db                 | PostgreSQL             | 5432 | Data storage                             |

---

## Local Development

**Prerequisites:**
- Docker & Docker Compose
- (Optional) Node.js, Python, Java for local builds

**Steps:**
```bash
git clone https://github.com/AET-DevOps25/team-evil-jenkins.git
cd team-evil-jenkins
cp .env.example .env 
docker compose up --build
```
- Access frontend: http://localhost:3000


---

## Kubernetes & Cloud Deployment

**Kubernetes (Helm):**
- Prerequisites: `kubectl`, `helm`, AWS CLI (for cloud)
- Edit `helm/team-evil-jenkins/values.yaml` for your environment.
- Deploy:
  ```bash
  helm upgrade --install team-evil-jenkins ./helm/team-evil-jenkins -n team-evil-jenkins
  ```
- For AWS: see `terraform/README.md` 

---




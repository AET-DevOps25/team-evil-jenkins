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

## Requirements

### Functional Requirements
- Users can register and log in using Auth0 authentication.
- Users can create, view, and update their profiles, including uploading an avatar.
- The system matches users based on sport interests, bio, and skill level using a GenAI-powered engine.
- Users can view a list of suggested matches and send/receive messages in real time.
- Location data is used to suggest nearby matches.
- All user, location, and matching data is persisted in a PostgreSQL database.
- Admins can monitor system health and metrics via Grafana and Prometheus dashboards.



### User Stories / Use Cases
- As a new user, I want to quickly register and set up my profile so I can start finding sports partners.
- As a user, I want to see a list of people who share my sports interests and live nearby.
- As a user, I want to chat with my matches in real time.
- As an admin, I want to monitor the health of all services from a central dashboard.



## Overview

The core functionality of the application is to connect individuals with shared interests in outdoor sports and physical activities. Users create profiles, share their bio, specify their preferred sports and skill level, and discover potential partners or groups in their area. The platform offers recommendations, matching, and communication tools to reduce the time and effort required to find activity partners.

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
| matching-service   | Spring Boot (Java)     | 8083 | Match history persistence, ranking, API  |
| genai              | FastAPI (Python)       | 8000 | AI/LLM matching, embeddings              |
| messaging-service  | Spring Boot (Java)     | 8082 | Messaging, WebSocket                     |
| api-gateway        | NGINX + Lua            | 80   | API gateway, JWT, CORS, proxy            |
| db                 | PostgreSQL             | 5432 | Data storage                             |
| grafana            | Grafana                | 3001 | Monitoring dashboard                     |
| prometheus         | Prometheus             | 9090 | Metrics collection and monitoring         |

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
./build-all.sh
```
- Access frontend: http://localhost:3000

The `build-all.sh` script will automatically:
- Build the frontend (client)
- Build all backend Java services
- Start Docker Compose with all services

You only need to run this script after a fresh clone or when dependencies change.

---

## Kubernetes & Cloud Deployment

**Kubernetes (Helm):**
- Prerequisites: `kubectl`, `helm`, AWS CLI (for cloud)
- Deploy:
  ```bash
  helm upgrade --install team-evil-jenkins ./helm/team-evil-jenkins -n team-evil-jenkins
  ```
- For AWS: see [`terraform/README.md`](terraform/README.md) 

---




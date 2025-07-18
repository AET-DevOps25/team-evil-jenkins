# MatchingService Microservice

This service provides REST APIs for user matchmaking, integrating with a GenAI-powered engine to generate structured match recommendations (with explanations, scores, and common preferences) and persisting match history in a Postgres database.

## Features
- **POST /matching/partners/{userId}**: Computes and returns the best-matching users for a given user, using GenAI for ranking and explanations. Results are persisted in the database.
- **GET /matching/history/{userId}**: Retrieves the most recent match results for a user, including explanations, scores, and common preferences.
- Integrates with UserService and LocationService to fetch user profiles and locations.
- Uses GenAI (via HTTP) to generate structured match data.
- Persists match results in Postgres using JPA/Hibernate.

## Architecture
- **Language:** Java (Spring Boot)
- **Persistence:** Postgres (JPA/Hibernate)
- **Inter-service communication:** REST (UserService, LocationService, GenAI)
- **DTOs:** All endpoints use DTOs for input/output (no direct entity exposure)


### Build & Run (Docker Compose)

```sh
docker compose up --build matchingservice
```

### API Example

#### POST /matching/partners/{userId}
Request triggers GenAI-powered matching and persists results.

```
POST /matching/partners/u1
```
Response:
```json
[
  {
    "id": "u2",
    "name": "Bob",
    "sportInterests": ["Tennis", "Swimming"]
  },
  ...
]
```

#### GET /matching/history/{userId}
Returns structured match history with explanations, scores, and preferences.

```
GET /matching/history/u1
```
Response:
```json
[
  {
    "matchedUserId": "u2",
    "score": 0.92,
    "explanation": "Both play tennis regularly",
    "commonPreferences": ["Tennis"],
    "createdAt": "2025-07-15T16:00:00Z"
  },
  ...
]
```

## Development
- See `src/test/java/matchingservice/MatchingServiceTest.java` and `MatchingControllerTest.java` for unit and controller tests.
- DTOs are defined in `src/main/java/matchingservice/dto/` and shared `model` module.
- GenAI integration logic is in `GenAiClient.java`.

## Requirements
- Java 17+
- Docker (for containerized deployment)
- Postgres database
- Running UserService, LocationService, and GenAI microservices



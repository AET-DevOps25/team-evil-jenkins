# Microservice API Documentation

This document provides an overview and API specifications for the microservices. Each service is designed to handle a specific domain, ensuring a modular and scalable architecture.

## User Service

The **User Service** manages all user-related operations, including creation, retrieval, updates, and deletion of user profiles. It also supports querying for nearby users based on their registered location. This service acts as the central repository for user data within the system.

### API Endpoints

* **`GET /user/{id}`**: Retrieves a specific user's profile by their ID.

* **`POST /user`**: Creates a new user profile.

* **`GET /user`**: Fetches a list of all registered users.

* **`PUT /user/{id}`**: Updates an existing user's profile information.

* **`DELETE /user/{id}`**: Deletes a user profile by their ID.

* **`GET /user/{id}/nearby`**: Finds users located within a specified radius of a given user. Communicates with location service for that

## Messaging Service

The **Messaging Service** facilitates real-time communication between users. It handles sending messages, retrieving conversation histories, and managing user contacts. This service ensures that users can interact seamlessly within the application.

### API Endpoints

* **`POST /messaging/send`**: Sends a message from one user to another.

* **`GET /messaging/conversation`**: Retrieves a paginated conversation history between two users.

* **`POST /messaging/contact`**: Adds another user to a user's contact list.

* **`GET /messaging/contacts/{userId}`**: Retrieves all contacts for a given user.

## Matching Service

The **Matching Service** is responsible for matchmaking operations, helping users find compatible partners based on various criteria. It also allows users to view their previous matches. This service is crucial for connecting users within the platform.

### API Endpoints

* **`POST /matching/partners/{userId}`**: Finds the best potential partners for a specified user.

* **`GET /matching/history/{userId}`**: Retrieves a list of previous matches for a given user.

## Location Service

The **Location Service** manages and tracks user locations. It provides functionalities to update user locations, retrieve a user's current location, and search for other users within a specific geographical area. This service underpins location-based features across the application.

### API Endpoints

* **`POST /location/update`**: Updates or sets a user's current geographical location.

* **`GET /location/{id}`**: Retrieves the current location of a user by their ID.

* **`GET /location/nearby`**: Searches for users located near a given point within a specified radius.

* **`GET /location/all`**: Retrieves all stored location records.







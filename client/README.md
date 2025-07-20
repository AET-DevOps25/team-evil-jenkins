# Sport Matcher - Frontend Client

This directory contains the frontend application for the Sport Matcher platform. It is a modern single-page application (SPA) built with **React** and bundled with **Vite** for a fast and efficient development experience.

---

## Table of Contents
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Environment Configuration](#environment-configuration)
- [Local Development](#local-development)
- [Available Scripts](#available-scripts)
- [Dockerization](#dockerization)

---

## Key Features

- **Component-Based UI:** Leverages React for building a modular and maintainable user interface.
- **Client-Side Routing:** Uses `react-router-dom` to handle navigation between different pages (`/profile`, `/matching`, `/chat`, etc.) without full page reloads.
- **Secure Authentication:** Integrates with **Auth0** using the `@auth0/auth0-react` SDK for robust user login, registration, and session management. Access tokens are automatically handled and sent with API requests.
- **Real-Time Messaging:** Implements a live chat feature using `@stomp/stompjs` and `sockjs-client` to connect to the backend WebSocket endpoints for instant message delivery.
- **Custom Contexts:** Utilizes React's Context API (e.g., `NotificationProvider`) for managing global state like notifications.

## Application Structure

The application's primary components are organized within the `src/` directory.

### Pages (`src/pages`)

The main views of the application are:

-   **`LandingPage.jsx`**: The initial page for unauthenticated users.
-   **`ProfilePage.jsx`**: Allows users to view and update their profile information, including their bio, interests, and profile picture.
-   **`MatchingPage.jsx`**: The core feature where users can view potential matches, see their compatibility scores, and like or dislike profiles.
-   **`MessagesPage.jsx`**: The real-time chat interface for users to communicate with their matches.
-   **`CallbackPage.jsx`**: Handles the redirect from Auth0 after a user successfully logs in.
-   **`SignInPage.jsx` / `SignUpPage.jsx`**: Simple pages that trigger the Auth0 login/signup flows.
-   **`LogoutPage.jsx`**: Handles the user logout process.
-   **`NotFoundPage.jsx`**: A fallback page for any invalid URLs.

## Technology Stack

- **Framework:** [React](https://react.dev/)
- **Build Tool:** [Vite](https://vitejs.dev/)
- **Routing:** [React Router](https://reactrouter.com/)
- **Authentication:** [Auth0](https://auth0.com/)
- **Real-time Communication:** [STOMP.js](https://stomp-js.github.io/) over SockJS
- **Linting:** ESLint

## Environment Configuration

The application requires specific environment variables for Auth0 to function correctly. These are **not** stored in this directory.

Instead, the Vite configuration (`vite.config.js`) is set up to load variables from the **project root's `.env` file**. It securely exposes only the necessary `VITE_` prefixed variables to the client-side code.

**Required Variables** (in root `.env`):
```
VITE_AUTH0_DOMAIN=your-auth0-domain
VITE_AUTH0_CLIENT_ID=your-auth0-client-id
VITE_AUTH0_AUDIENCE=your-auth0-api-audience
```

## Local Development

To run the client locally for development, first ensure all dependencies are installed.

```bash
# Navigate to the client directory
cd client

# Install dependencies
npm install
```

Then, start the Vite development server:

```bash
npm run dev
```

This will launch the application on **`http://localhost:3000`**. The server features Hot Module Replacement (HMR) for instant feedback on code changes. All API and WebSocket requests are automatically proxied to the NGINX gateway at `http://localhost:80` as defined in `vite.config.js`, preventing any CORS issues.

## Available Scripts

- `npm run dev`: Starts the development server.
- `npm run build`: Bundles the app for production into the `dist/` folder.
- `npm run lint`: Lints the source code using ESLint.
- `npm run preview`: Serves the production build locally to preview its behavior.

## Dockerization

The included `Dockerfile` creates a lightweight, production-ready image for the client.

It uses a **multi-stage build** process:
1.  **Build Stage:** Uses a `node` base image to install dependencies and run `npm run build`, creating an optimized bundle of static assets.
2.  **Final Stage:** Copies the built assets from the `dist/` directory into a lean `nginx` container. The NGINX server is pre-configured to serve the React application and correctly handle client-side routing.

This container is used in the main `docker-compose.yml` and is the target for Kubernetes deployments.

# SportMatch Frontend

SportMatch is a React-based web application designed to connect users with like-minded athletes in their area for various sports activities. Users can create profiles, set their sport preferences, skill levels, and availability, and then get matched with compatible partners. The application also includes a real-time messaging system for matched users to communicate.

## Features

* **User Authentication**: Secure user login and registration powered by Auth0.
* **User Profiles**: Comprehensive profiles allowing users to specify:
    * First Name, Last Name, Email
    * Biography
    * Preferred Sports (with an option to add custom sports)
    * Skill Level (Beginner, Intermediate, Advanced)
    * Availability (days of the week and time slots)
    * Automatic location detection and display.
* **Matching System**:
    * AI-powered matching based on user preferences (sports, skill level, availability, location).
    * Displays a match percentage and an explanation for why users are matched.
    * Ability to load more matches.
* **Real-time Messaging**:
    * Connects users for direct communication via WebSockets (STOMP over SockJS).
    * Displays contacts and conversation history.
    * Sends and receives messages in real-time.
* **Notifications**: In-app toast notifications for user feedback (e.g., profile saved, errors).
* **Responsive Design**: Utilizes CSS for a consistent look across devices.
* **Landing Page**: Informative landing page with sections for "How It Works," "Testimonials," and "Contact Us."

## Tech Stack

* **Frontend**: React.js
* **Routing**: React Router DOM
* **Authentication**: Auth0 React SDK
* **State Management**: React Hooks (useState, useEffect, useContext, useRef, useCallback, useMemo)
* **Real-time Communication**: `@stomp/stompjs` and `sockjs-client`
* **API Communication**: `fetch` API
* **Styling**: Plain CSS (`.css` modules)
* **Geolocation**: Browser's `navigator.geolocation` API
* **Reverse Geocoding**: Nominatim OpenStreetMap API
* **Build Tool**: Vite (implied by `import.meta.env`)

## Installation

To get the SportMatch frontend up and running on your local machine, follow these steps:

1.  **Clone the Repository**:

    ```bash
    git clone <repository-url>
    cd sportmatch-frontend/client # Navigate into the client directory
    ```

2.  **Install Dependencies**:

    ```bash
    npm install
    # or
    yarn install
    ```

3.  **Environment Variables**:
    Create a `.env` file in the `client/` directory and add your Auth0 configuration. These variables are crucial for the application to connect to your Auth0 tenant and the backend API.

    ```dotenv
    VITE_AUTH0_DOMAIN="YOUR_AUTH0_DOMAIN"
    VITE_AUTH0_CLIENT_ID="YOUR_AUTH0_CLIENT_ID"
    VITE_AUTH0_AUDIENCE="YOUR_AUTH0_AUDIENCE"
    # Example: http://localhost:80 or [https://api.yourdomain.com](https://api.yourdomain.com) for production
    VITE_API_URL="YOUR_BACKEND_API_URL" 
    ```
    * `VITE_AUTH0_DOMAIN`: Your Auth0 domain (e.g., `dev-youraccount.us.auth0.com`).
    * `VITE_AUTH0_CLIENT_ID`: Your Auth0 application's client ID.
    * `VITE_AUTH0_AUDIENCE`: The audience identifier for your Auth0 API (e.g., `https://api.sportmatch.com`).
    * `VITE_API_URL`: The base URL of your backend API. If running locally with Docker/Nginx, this might be `http://localhost:80`. In a Kubernetes environment, it would be your API's public domain.

4.  **Run the Application**:

    ```bash
    npm run dev
    # or
    yarn dev
    ```
    This will start the development server, usually on `http://localhost:5173` (or another available port). The application will automatically open in your browser.

## Available Scripts

In the `client/` directory, you can run:

* `npm run dev`: Starts the development server.
* `npm run build`: Builds the app for production to the `dist` folder.
* `npm run lint`: Lints the code using ESLint.
* `npm run preview`: Serves the production build locally.
* `npm test`: Launches the test runner.

## Project Structure

## Component Documentation

* **`App.jsx`**: The root component that sets up React Router and defines all application routes, including protected routes using `RequireAuth`. It also integrates `useRegisterUser` and `useUpdateLocation` hooks globally.
* **`Header.jsx`**: Displays the navigation bar, including brand logo, authentication links (Login/Sign Up/Logout), and user-specific navigation when authenticated.
* **`Notification.jsx`**: A presentational component for displaying individual toast notifications.
* **`LoadingSpinner.jsx`**: A simple component to display a loading message.
* **`ContactSection.jsx`**: Provides a contact form for users to send messages, utilizing the `NotificationContext` for feedback.
* **`Testimonials.jsx`**: Renders a list of user testimonials on the landing page.
* **`HeroSection.jsx`**: The main introductory section of the landing page.
* **`CTASection.jsx`**: A call-to-action section on the landing page.
* **`HowItWorks.jsx`**: Explains the three-step process of using the application.
* **`Footer.jsx`**: The application's footer with company, legal, and social links.
* **`RequireAuth.jsx`**: A Higher-Order Component (HOC) that checks user authentication status using Auth0 and redirects to the sign-in page if not authenticated.
* **`ProfilePage.jsx`**: Allows authenticated users to view and update their profile details, including personal info, sports interests, skill level, and availability. It fetches and saves data to the backend API and uses Nominatim for reverse geocoding the user's location.
* **`MessagesPage.jsx`**: Provides a real-time chat interface for users to communicate with their matches. It leverages WebSockets (STOMP) for live message exchange and integrates with the messaging backend.
* **`MatchingPage.jsx`**: Displays a list of matched partners based on user preferences. It fetches match history and live matching data from the backend, and allows users to initiate conversations with matches.
* **`LandingPage.jsx`**: Composes various marketing and informational sections to form the public-facing landing page.
* **`SignInPage.jsx` / `SignUpPage.jsx`**: Simple pages that redirect the user to Auth0 for login or signup flows.
* **`CallbackPage.jsx`**: Handles the redirect from Auth0 after successful authentication.
* **`LogoutPage.jsx`**: Handles the redirect from Auth0 after logout and provides a brief loading state before navigating home.
* **`NotFoundPage.jsx`**: A generic 404 page for routes that don't exist.

## Contexts and Hooks

* **`NotificationContext.jsx`**: Provides a global context for displaying ephemeral "toast" notifications across the application. The `useNotification` hook allows any component to trigger a notification.
* **`useRegisterUser.js`**: An effect hook that runs once after a user successfully authenticates with Auth0. It sends the user's initial profile data to the backend to ensure a corresponding user record exists in the database.
* **`useUpdateLocation.js`**: An effect hook responsible for continuously updating the user's geolocation in the backend. It uses `navigator.geolocation.watchPosition` for live updates and `getCurrentPosition` for periodic fallback, sending data with the Auth0 access token.

## Styling

The application uses plain CSS files for styling, organized by component or page. Global styles are in `index.css` and `App.css`, while specific overrides or component-level styles are found in the `styles/` directory.

## Testing

The project includes basic testing setup with Jest and React Testing Library.

* `App.test.js`: Contains a simple example test for the main `App` component.
* `setupTests.js`: Configures `jest-dom` for custom Jest matchers.
* `reportWebVitals.js`: Integrates `web-vitals` to measure and report on performance metrics.

## Future Improvements / To-Do

* **Distance Calculation**: The matching page currently shows `distance: 0` for matches. This needs to be implemented and returned by the backend matching service.
* **Notifications Bell**: The header includes a placeholder for a notification bell (`<img src="/images/icon-bell.svg" alt="Bell" />`), suggesting future integration for in-app notifications beyond just toasts.
* **Map View**: The matching page includes a placeholder for a "Map view coming soon…", indicating plans to visualize matches on a map.
* **Group Chat**: The `MessagesPage` mock data includes "SF Runners Group", suggesting potential future support for group conversations.
* **Refined Match Explanations**: The `explanation` field in matching results could be made more detailed or dynamic.
* **User Profile Image Upload**: Currently, the avatar is pulled from Auth0. Functionality to allow users to upload custom profile pictures could be added.
* **Input Validation**: Enhance client-side input validation for forms (e.g., contact form, profile page) beyond just `required` attributes.
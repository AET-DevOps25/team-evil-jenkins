import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Auth0Provider } from '@auth0/auth0-react';
import './index.css';
import './styles/formOverrides.css';
import { NotificationProvider } from './contexts/NotificationContext.jsx';
import './styles/Notification.css';
import App from './App.jsx';

// Debug: Log environment variables
console.log('VITE_AUTH0_DOMAIN:', import.meta.env.VITE_AUTH0_DOMAIN);
console.log('VITE_AUTH0_CLIENT_ID:', import.meta.env.VITE_AUTH0_CLIENT_ID);
console.log('VITE_AUTH0_AUDIENCE:', import.meta.env.VITE_AUTH0_AUDIENCE);
console.log('All import.meta.env:', import.meta.env);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Auth0Provider
      domain={import.meta.env.VITE_AUTH0_DOMAIN}
      clientId={import.meta.env.VITE_AUTH0_CLIENT_ID}
      authorizationParams={{
        redirect_uri: window.location.origin + '/callback',
        audience: import.meta.env.VITE_AUTH0_AUDIENCE,
      }}
    >
      <NotificationProvider>
        <BrowserRouter>
        <App />
        </BrowserRouter>
      </NotificationProvider>
    </Auth0Provider>
  </StrictMode>
);

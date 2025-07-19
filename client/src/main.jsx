import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Auth0Provider } from '@auth0/auth0-react';
import './index.css';
import './styles/formOverrides.css';
import { NotificationProvider } from './contexts/NotificationContext.jsx';
import './styles/Notification.css';
import App from './App.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Auth0Provider
      domain={process.env.AUTH0_DOMAIN}
      clientId={process.env.AUTH0_CLIENT_ID}
      authorizationParams={{
        redirect_uri: window.location.origin + '/callback',
        audience: process.env.AUTH0_AUDIENCE,
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

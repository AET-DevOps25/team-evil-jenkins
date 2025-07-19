import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from '../components/LoadingSpinner';

// Simple page shown after Auth0 redirects back to our app post-logout.
// It briefly shows a spinner and then navigates to the landing page (/).
export default function LogoutPage() {
  const navigate = useNavigate();

  useEffect(() => {
    // Give users brief feedback, then redirect.
    const id = setTimeout(() => navigate('/', { replace: true }), 1000);
    return () => clearTimeout(id);
  }, [navigate]);

  return <LoadingSpinner text="Logging out…" />;
}

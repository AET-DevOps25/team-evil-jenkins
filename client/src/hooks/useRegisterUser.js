import { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

/**
 * After successful Auth0 authentication, this hook ensures the
 * corresponding user record exists in the backend database.
 * It runs once per session when the user becomes authenticated.
 */
export default function useRegisterUser() {
    const { isAuthenticated, user, getAccessTokenSilently } = useAuth0();
    // API URL configuration for different environments
    // Docker: Frontend on :3000, nginx gateway on :80
    // Kubernetes: Frontend and API on separate domains
    const API_URL = (window.location.hostname === 'localhost' ? 'http://localhost:80' : `https://api.${window.location.hostname}`);

    useEffect(() => {
        if (!isAuthenticated || !user) return;

        (async () => {
            try {
                const token = await getAccessTokenSilently();
                const res = await fetch(`${API_URL}/user/`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        id: user.sub,
                        name: user.name || user.nickname || '',
                        email: user.email,
                        picture: user.picture,
                    }),
                });
                if (!res.ok) {
                    console.error('POST /user/ failed', res.status, await res.text());
                } else {
                    console.log('User registered in backend');
                    console.log(res);
                    console.log(user);
                }
            } catch (err) {
                // eslint-disable-next-line no-console
                console.error('Failed to register user in backend', err);
            }
        })();
    }, [isAuthenticated, user, getAccessTokenSilently]);
}

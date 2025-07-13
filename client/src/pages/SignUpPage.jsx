import { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

export default function SignUpPage() {
    const { loginWithRedirect, isLoading } = useAuth0();

    useEffect(() => {
        loginWithRedirect({ screen_hint: 'signup' });
    }, [loginWithRedirect]);

    return (
        <main style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
            {isLoading ? 'Redirecting to Auth0…' : 'Loading…'}
        </main>
    );
}
import { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

export default function SignInPage() {
    const { loginWithRedirect, isLoading } = useAuth0();

    useEffect(() => {
        loginWithRedirect();
    }, [loginWithRedirect]);

    return (
        <main style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
            <p>{isLoading ? 'Redirecting to Auth0…' : 'Loading…'}</p>
        </main>
    );
}



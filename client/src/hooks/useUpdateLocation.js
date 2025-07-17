import { useEffect, useRef } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useNotification } from '../contexts/NotificationContext';

/**
 * Continuously send the users geolocation to the Location micro-service.
 *
 * How it works
 * 1. As soon as the user is authenticated, we start a `navigator.geolocation.watchPosition`
 *    listener so the browser notifies us whenever the coordinates change.
 * 2. We also trigger a periodic fallback (30 s by default) with `getCurrentPosition` in case
 *    the OS is stingy with updates.
 * 3. Every position update is POSTed to `/location/update` with the Auth0 access token so the
 *    API-gateway (Lua OIDC) accepts the request.
 *
 * The hook cleans up on unmount or when the user logs out.
 *
 * @param {number} [intervalMs=30000] Fallback heartbeat interval in milliseconds.
 */
export default function useUpdateLocation(intervalMs = 30_000) {
    const { isAuthenticated, user, getAccessTokenSilently } = useAuth0();
    const { notify } = useNotification();
    const watchIdRef = useRef(null);
    const API = import.meta.env.VITE_API_URL || 'http://localhost:80';

    useEffect(() => {
        if (!isAuthenticated || !user || !('geolocation' in navigator)) return;

        let cancelled = false;

        async function sendLocation({ latitude, longitude }) {
            try {
                const token = await getAccessTokenSilently();
                if (cancelled) return;
                const qs = new URLSearchParams({
                    userId: user.sub,
                    latitude: latitude.toString(),
                    longitude: longitude.toString(),
                }).toString();
                console.log(qs);
                console.log('latitude', latitude);
                console.log('longitude', longitude);
                console.log('user.sub', user.sub);
                await fetch(`${API}/location/update?${qs}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                });
            } catch (err) {
                /* eslint-disable no-console */
                console.error('Location update failed', err);
                /* eslint-enable no-console */
            }

        }

        // Browser live updates
        watchIdRef.current = navigator.geolocation.watchPosition(
            pos => sendLocation(pos.coords),
            err => notify({ type: 'error', message: 'Geolocation error. Make sure you have enabled location sharing in your browser settings.' }),
            { enableHighAccuracy: false, maximumAge: 10000, timeout: 20000 },
        );

        // Periodic fallback
        const timer = setInterval(() => {
            navigator.geolocation.getCurrentPosition(
                pos => sendLocation(pos.coords),
                err => console.error('Geolocation error', err),
            );
        }, intervalMs);

        return () => {
            cancelled = true;
            clearInterval(timer);
            if (watchIdRef.current !== null) {
                navigator.geolocation.clearWatch(watchIdRef.current);
            }
        };
    }, [isAuthenticated, user, getAccessTokenSilently, intervalMs, API]);
}

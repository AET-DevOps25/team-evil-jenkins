import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig(({ mode }) => {
  // Load env file from the project root (parent directory)
  const env = loadEnv(mode, path.resolve(__dirname, '..'), '');

  return {
    plugins: [react()],
    define: {
      // Explicitly expose VITE_ prefixed env vars to the client
      'import.meta.env.VITE_AUTH0_DOMAIN': JSON.stringify(env.VITE_AUTH0_DOMAIN),
      'import.meta.env.VITE_AUTH0_CLIENT_ID': JSON.stringify(env.VITE_AUTH0_CLIENT_ID),
      'import.meta.env.VITE_AUTH0_AUDIENCE': JSON.stringify(env.VITE_AUTH0_AUDIENCE),
    },
    server: {
      port: 3000,
      proxy: {
        // forward any call that starts with these prefixes to the gateway
        '/user': 'http://localhost:80',
        '/location': 'http://localhost:80',
        '/messaging': 'http://localhost:80',
        '/matching': 'http://localhost:80',
        '/genai': 'http://localhost:80',
        '/ws': {      // WebSocket/STOMP endpoint
          target: 'ws://localhost:80',
          ws: true,
        },
      },
    },
  };
});
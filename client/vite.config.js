import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
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
});
# NGINX API Gateway

This directory contains the resources for the NGINX gateway that fronts all backend micro-services.

| File | Purpose |
|------|---------|
| `Dockerfile` | Builds the NGINX image used in Docker Compose / Kubernetes. Copies `nginx.conf` and Lua scripts, installs `lua-resty-openidc`, and sets up a non-root user. |
| `nginx.conf` | Main NGINX configuration. Acts as a reverse proxy to micro-services, sets dynamic CORS headers, and routes WebSocket traffic. Includes the Lua module for JWT/OIDC validation. |
| `oidc.lua` | Lua script executed by `access_by_lua_block` & `header_filter_by_lua_block` to validate JWT tokens, inject user headers, and ensure CORS headers are returned on **all** responses (including 401/403). |

## Key Features

1. **JWT / OIDC Validation**  
   Incoming requests pass through `oidc.lua`, which verifies the bearer token (JWT) issued by Auth0. Invalid or missing tokens receive a `401`/`403`.

2. **Dynamic CORS Handling**  
   CORS headers are set using `$http_origin`, ensuring correct origins and avoiding duplicate headers (especially for WebSocket endpoints like `/ws`).

3. **Service Routing**  
   Path-based routing proxies traffic to the appropriate backend:
   - `/user/` → `user-service`
   - `/location/` → `location-service`
   - `/matching/` & `/genai/` → `matching-service` / `genai`
   - `/ws/` → `messaging-service` (WebSocket/SockJS)

4. **Static Assets**  
   Serves the built React SPA (`client`) directly, reducing the need for a separate web server.

## Development Tips

- **Edit & Test Locally**: Modify `nginx.conf`, then rebuild the container with `docker compose up --build nginx`.
- **Helm Deployment**: In Kubernetes, the same `nginx.conf` is mounted via ConfigMap (`nginx-gateway-configmap.yaml`). Updating the config requires a Helm upgrade and pod restart.
- **Debugging**: Attach to the container (`docker exec -it <nginx-container> /bin/sh`) and inspect logs in `/var/log/nginx/` for troubleshooting.

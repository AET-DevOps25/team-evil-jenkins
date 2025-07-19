-- Authentication is now handled by backend services
-- This nginx gateway only handles CORS preflight requests
-- All JWT validation moved to individual microservices

-- Skip JWT validation for OPTIONS requests (CORS preflight)
-- Note: CORS headers are handled by nginx config to avoid duplicates
if ngx.var.request_method == "OPTIONS" then
  ngx.status = 204
  return ngx.exit(204)
end

-- Log that authentication is handled downstream
ngx.log(ngx.INFO, "Request forwarded to backend services for authentication")

-- Continue to upstream - backend services will validate JWT tokens

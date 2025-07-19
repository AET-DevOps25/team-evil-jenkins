-- Simple Auth0 JWT verification using lua-resty-openidc
-- Expects ENV vars AUTH0_DOMAIN and AUTH0_AUDIENCE passed to container

local cjson = require "cjson"
local oidc = require "resty.openidc"

-- Always set CORS headers so that browser receives them even on 401/403
ngx.header["Access-Control-Allow-Origin"] = "*"
ngx.header["Access-Control-Allow-Methods"] = "GET, POST, PUT, PATCH, DELETE, OPTIONS"
ngx.header["Access-Control-Allow-Headers"] = "Authorization,Content-Type"

-- Skip JWT validation for OPTIONS requests (CORS preflight)
if ngx.var.request_method == "OPTIONS" then
  ngx.header["Access-Control-Max-Age"] = "86400"
  ngx.status = 204
  return ngx.exit(204)
end

local opts = {
  discovery = string.format("https://%s/.well-known/openid-configuration", os.getenv("AUTH0_DOMAIN")),
  token_signing_alg_values_expected = {"RS256"},
  accept_none_alg = false,
  -- audience check (Auth0 calls this "aud")
  client_id = os.getenv("AUTH0_AUDIENCE"),
  -- we are validating bearer tokens only, no redirects
  redirect_uri = "https://dummy", -- required but unused in bearer mode
  ssl_verify = "yes"
}

local res, err = oidc.bearer_jwt_verify(opts)

if err then
  ngx.status = 401
  ngx.header.content_type = "application/json; charset=utf-8"
  ngx.say(cjson.encode({ error = "unauthorized", message = err }))
  return ngx.exit(ngx.HTTP_UNAUTHORIZED)
end
-- token is valid, continue to upstream

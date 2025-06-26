# OpenResty provides NGINX with LuaJIT support
FROM openresty/openresty:1.25.3.1-alpine

# Install Lua dependencies for JWT / OIDC verification
RUN apk add --no-cache lua-resty-http lua-resty-jwt lua-resty-session git \
    && luarocks install lua-resty-openidc 1.7.5

# Copy Lua script for Auth0 validation (compose will mount nginx.conf)
COPY oidc.lua /etc/nginx/oidc.lua

# Use the default OpenResty entrypoint
CMD ["openresty", "-g", "daemon off;"]

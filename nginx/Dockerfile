# OpenResty provides NGINX with LuaJIT support
FROM fabiocicerchia/nginx-lua:1.29.0-almalinux


# Copy Lua script for Auth0 validation (compose will mount nginx.conf)
COPY oidc.lua /etc/nginx/oidc.lua

CMD ["nginx", "-g", "daemon off;"]

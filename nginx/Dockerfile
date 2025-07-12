# OpenResty provides NGINX with LuaJIT support
FROM ghcr.io/aet-devops25/nginx-lua-base:latest

COPY oidc.lua /etc/nginx/oidc.lua

CMD ["nginx", "-g", "daemon off;"]

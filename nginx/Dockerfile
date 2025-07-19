# OpenResty provides NGINX with LuaJIT support
FROM fabiocicerchia/nginx-lua:1.29.0-almalinux

USER root
RUN yum install -y epel-release && \
    yum install -y luarocks gcc make && \
    luarocks install lua-cjson && \
    luarocks install lua-resty-openidc

# Copy Lua script for Auth0 validation (compose will mount nginx.conf)
COPY oidc.lua /etc/nginx/oidc.lua
COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]

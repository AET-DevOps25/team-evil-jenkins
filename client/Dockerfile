# --- build React app ---
FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci        # or `npm install` if you prefer

COPY . .
RUN npm run build   # creates /app/build

# --- runtime image ---
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
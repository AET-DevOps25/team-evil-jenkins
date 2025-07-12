FROM node:18-alpine
WORKDIR /app

# copy the built bundle provided by the workflow
COPY dist/ ./dist

# tiny static server (≈900 kB)
RUN npm install -g serve@14

EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]
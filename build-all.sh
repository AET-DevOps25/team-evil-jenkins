#!/bin/bash
set -e

echo "Building client..."
cd client
npm install
npm run build
cd ..

echo "Building services..."
cd server
./gradlew clean build
cd ..

echo "Starting Docker Compose..."
docker compose up --build

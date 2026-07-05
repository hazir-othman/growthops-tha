#!/bin/bash
set -e

echo "=== Travel Insurance Portal Deployment ==="

# Create database volume directory with write permissions
echo "Creating local database directory..."
mkdir -p data
chmod 777 data

# Shutdown existing containers
echo "Stopping existing containers..."
docker compose down || true

# Build and start container
echo "Building and launching containerized application..."
docker compose up -d --build

echo "=== Deployment Completed Successfully ==="
echo "Application URL: http://localhost:8081"
echo "To check container logs, run: docker compose logs -f"

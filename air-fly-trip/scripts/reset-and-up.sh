#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "[air-fly-trip] Stopping stack and removing containers, networks, and volumes..."
docker compose down --volumes --remove-orphans

echo "[air-fly-trip] Building and starting stack from zero..."
docker compose up -d --build --force-recreate

echo "[air-fly-trip] Done. Current status:"
docker compose ps

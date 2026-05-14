@echo off
setlocal

cd /d "%~dp0.."

echo [air-fly-trip] Stopping stack and removing containers, networks, and volumes...
docker compose down --volumes --remove-orphans
if errorlevel 1 goto :error

echo [air-fly-trip] Building and starting stack from zero...
docker compose up -d --build --force-recreate
if errorlevel 1 goto :error

echo [air-fly-trip] Done. Current status:
docker compose ps
if errorlevel 1 goto :error

goto :eof

:error
echo [air-fly-trip] Command failed.
exit /b 1

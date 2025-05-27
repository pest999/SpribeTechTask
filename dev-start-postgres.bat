@echo off
echo ===============================================
echo Starting PlayerController API with PostgreSQL
echo Auto-shutdown: 10 minutes
echo ===============================================

echo Step 1: Starting PostgreSQL with Docker...
cd /d %~dp0
docker-compose up -d postgres

echo Waiting for PostgreSQL to start (10 seconds)...
timeout /t 10 /nobreak > nul

REM Check if PostgreSQL container is running
docker ps | findstr "stribe-db" > nul
if errorlevel 1 (
    echo ERROR: PostgreSQL container failed to start!
    pause
    exit /b 1
)

echo Step 2: Starting auto-shutdown timer (10 minutes)...
start /min cmd /c "timeout /t 600 /nobreak > nul && echo Auto-shutdown triggered... && taskkill /f /im java.exe && docker-compose down"

echo Step 3: Starting Spring Boot application...
echo Profile: postgresql (PostgreSQL)
echo Port: 8082
echo Auto-shutdown in 10 minutes...
echo Press Ctrl+C to stop manually

mvn spring-boot:run -Dspring-boot.run.profiles=postgresql

echo Application stopped.
echo Stopping PostgreSQL containers...
docker-compose down
echo PostgreSQL stopped.
pause 
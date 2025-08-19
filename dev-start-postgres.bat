@echo off
echo ===============================================
echo Starting PlayerController API with PostgreSQL
echo Auto-shutdown: 10 minutes
echo ===============================================

cd /d %~dp0

echo Step 1: Starting PostgreSQL with Docker...
docker-compose up -d postgres

echo Waiting for PostgreSQL to be ready...
:check_postgres
docker exec flamingo-db pg_isready -U flamingo_user > nul 2>&1
if errorlevel 1 (
    timeout /t 2 /nobreak > nul
    goto check_postgres
)

echo PostgreSQL is ready!

echo Step 2: Starting auto-shutdown timer (10 minutes)...
start /min cmd /c "timeout /t 600 /nobreak > nul && echo Auto-shutdown triggered... && docker-compose down"

echo Step 3: Starting Spring Boot application...
echo Profile: postgresql (PostgreSQL)
echo Port: 8082
echo Force UTC timezone to avoid PostgreSQL issues
echo Press Ctrl+C to stop manually

set TZ=UTC
set JAVA_OPTS=-Duser.timezone=UTC -Duser.country=US -Duser.language=en
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql -Duser.timezone=UTC -Duser.country=US -Duser.language=en

echo Application stopped.
echo Stopping PostgreSQL containers...
docker-compose down -v
echo PostgreSQL stopped.
pause

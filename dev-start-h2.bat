@echo off
echo =========================================
echo Starting PlayerController API with H2
echo Auto-shutdown: 10 minutes
echo =========================================

echo Step 1: Starting auto-shutdown timer (10 minutes)...
start /min cmd /c "timeout /t 600 /nobreak > nul && echo Auto-shutdown triggered... && taskkill /f /im java.exe"

echo Step 2: Starting Spring Boot application...
echo Profile: dev (H2 In-Memory)
echo Port: 8082
echo H2 Console: http://localhost:8082/h2-console
echo Auto-shutdown in 10 minutes...
echo Press Ctrl+C to stop manually
echo.

cd /d %~dp0
mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo.
echo Application stopped.
pause
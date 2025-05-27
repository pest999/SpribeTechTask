@echo off
cls

echo ==========================================
echo        API Tests with Allure
echo ==========================================

echo.
echo 1. Cleanup...
taskkill /f /im java.exe > nul 2>&1
docker-compose down > nul 2>&1
if exist "allure-results" rmdir /s /q "allure-results" > nul 2>&1

echo 2. Starting PostgreSQL...
docker-compose up -d postgres
timeout /t 15 /nobreak > nul

echo 3. Starting Spring Boot app...
start /min call mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
timeout /t 60 /nobreak > nul

echo 4. Running tests...
call mvn test -Dspring.profiles.active=postgresql

echo 5. Opening Allure report...
echo NOTE: Allure server will start. Press Ctrl+C to stop when done viewing.
call mvn allure:serve

echo 6. Cleanup...
taskkill /f /im java.exe > nul 2>&1
docker-compose down > nul 2>&1

echo.
echo Done! Press any key to exit...
pause > nul 
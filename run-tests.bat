@echo off
cls

echo ==========================================
echo        API Tests with Allure              
echo ==========================================
echo.

echo 1. Cleaning up...
call mvn clean
if %ERRORLEVEL% NEQ 0 (
    echo Maven clean failed with error %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo 2. Starting PostgreSQL...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo Docker Compose failed with error %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo 3. Waiting for PostgreSQL to initialize (10 seconds)...
ping 127.0.0.1 -n 11 > nul

echo.
echo 4. Running tests...
call mvn test
set TEST_RESULT=%ERRORLEVEL%
if %TEST_RESULT% NEQ 0 (
    echo Tests failed with error %TEST_RESULT%
    echo But continuing to generate Allure reports...
)

echo.
echo 5. Generating Allure report...
call mvn allure:report
if %ERRORLEVEL% NEQ 0 (
    echo Report generation failed with error %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo 6. Serving Allure report...
echo Note: Press Ctrl+C to stop the Allure server when done
call mvn allure:serve

echo.
echo 7. Cleaning up Docker containers...
docker-compose down

echo.
if %TEST_RESULT% NEQ 0 (
    echo Tests completed with failures, but Allure report generated successfully!
) else (
    echo All tests passed and Allure report generated!
)
pause

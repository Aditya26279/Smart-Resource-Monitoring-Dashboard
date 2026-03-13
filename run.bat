@echo off
echo =======================================================
echo     Starting Smart Resource Monitoring Dashboard
echo =======================================================
echo.

echo [1/2] Building the project using Maven...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Maven build failed. Please make sure Maven is installed and working.
    pause
    exit /b %errorlevel%
)
echo.

echo [2/2] Launching the Spring Boot application...
echo The dashboard will be available at http://localhost:8080
echo.
java -jar target\dashboard-0.0.1-SNAPSHOT.jar

pause

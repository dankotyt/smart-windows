@echo off
echo Running tests with clean configuration...

:: Очищаем кэш Gradle и останавливаем демоны
call gradlew.bat --stop >nul 2>&1

:: Запускаем тесты с отключенным daemon и кэшем
call gradlew.bat :microservice-presets:check :microservice-analytics:check --no-daemon --no-build-cache --stacktrace

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Tests failed! Commit aborted.
    exit /b 1
)

echo All tests passed!
exit /b 0
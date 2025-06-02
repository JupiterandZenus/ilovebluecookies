@echo off
echo Discord RuneLite Integration Plugin Setup
echo ==========================================
echo.

echo Building the plugin...
call gradlew.bat build

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.
echo The compiled JAR file is located at: build\libs\DiscordRLIntegration-1.0.0.jar
echo.
echo To install:
echo 1. Copy the JAR file to your RuneLite plugins directory
echo 2. Restart RuneLite
echo 3. Enable the "Discord RL Integration" plugin
echo 4. Configure your Discord webhook URL in the plugin settings
echo.
echo For more information, see the README.md file.
echo.
pause 
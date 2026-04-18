@echo off
echo [1/3] Cleaning output folder...
if exist out rmdir /s /q out
mkdir out

echo [2/3] Collecting source files...
dir /s /b *.java > sources.txt

echo [3/3] Compiling...
javac -cp ".;lib\ojdbc17.jar" -d out @sources.txt
if %ERRORLEVEL% == 0 (
    echo.
    echo ============================================
    echo  BUILD SUCCESSFUL
    echo ============================================
) else (
    echo.
    echo ============================================
    echo  BUILD FAILED - Check errors above
    echo ============================================
)
del sources.txt
pause

@echo off
setlocal enableextensions
pushd %~dp0

cd ..

:: Clear the persistent file (data\budgetflow.txt)
if exist data\budgetflow.txt (
    type nul > data\budgetflow.txt
) else (
    mkdir data
    type nul > data\budgetflow.txt
)
echo Cleared persistent file: %cd%\data\budgetflow.txt

call gradlew clean shadowJar

cd build\libs
for /f "tokens=*" %%a in ('dir /b *.jar') do (
    set jarloc=%%a
)

java -jar %jarloc% < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.TXT

cd ..\..\text-ui-test

:: Remove whitespace from files using PowerShell and store in temporary files
powershell -Command "(Get-Content ACTUAL.TXT) -replace '\s','' | Set-Content ACTUAL_NO_WS.TXT"
powershell -Command "(Get-Content EXPECTED.TXT) -replace '\s','' | Set-Content EXPECTED_NO_WS.TXT"

:: Compare the whitespace-stripped files
FC ACTUAL_NO_WS.TXT EXPECTED_NO_WS.TXT >NUL && ECHO Test passed! || ECHO Test failed!

:: Clean up temporary files
del ACTUAL_NO_WS.TXT EXPECTED_NO_WS.TXT

popd

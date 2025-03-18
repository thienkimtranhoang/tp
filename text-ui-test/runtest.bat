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

FC ACTUAL.TXT EXPECTED.TXT >NUL && ECHO Test passed! || ECHO Test failed!

popd

#!/usr/bin/env bash
set -e

# Change to script directory
cd "${0%/*}"

cd ..

# Clear the persistent file (data/budgetflow.txt)
DATA_FILE="data/budgetflow.txt"
if [ -f "$DATA_FILE" ]; then
    > "$DATA_FILE"
else
    mkdir -p data
    > "$DATA_FILE"
fi
echo "Cleared persistent file: $(realpath "$DATA_FILE")"

./gradlew clean shadowJar

cd text-ui-test

java -jar $(find ../build/libs/ -mindepth 1 -print -quit) < input.txt > ACTUAL.TXT

cp EXPECTED.TXT EXPECTED-UNIX.TXT

# Check if dos2unix exists before using it
if command -v dos2unix >/dev/null 2>&1; then
    dos2unix EXPECTED-UNIX.TXT ACTUAL.TXT
else
    echo "dos2unix not found; skipping conversion"
fi

diff EXPECTED-UNIX.TXT ACTUAL.TXT
if [ $? -eq 0 ]; then
    echo "Test passed!"
    exit 0
else
    echo "Test failed!"
    exit 1
fi

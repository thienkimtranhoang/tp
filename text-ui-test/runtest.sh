#!/usr/bin/env bash
set -e

# Change to script directory
cd "${0%/*}"
cd ..

# Clear the persistent budgetflow file
DATA_FILE="text-ui-test/data/budgetflow.txt"
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

# Remove unwanted "Data loaded successfully" line from ACTUAL.TXT
if [[ "$(uname)" == "Darwin" ]]; then
    sed -i '' '/^Data loaded successfully/d' ACTUAL.TXT
else
    sed -i '/^Data loaded successfully/d' ACTUAL.TXT
fi

# Capture diff exit code using -w to ignore whitespace differences
set +e
diff -w EXPECTED-UNIX.TXT ACTUAL.TXT > diff.txt
diff_exit=$?
set -e

if [ $diff_exit -eq 0 ]; then
    echo "Test passed!"
    exit 0
else
    echo "Differences found between EXPECTED-UNIX.TXT and ACTUAL.TXT:"
    cat diff.txt
    echo "Test failed!"
    exit 1
fi

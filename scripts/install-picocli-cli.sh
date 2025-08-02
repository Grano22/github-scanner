#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BIN_DIR="$SCRIPT_DIR/../bin"
mkdir -p "$BIN_DIR"

JAR_NAME="picocli-4.7.7.jar"
URL="https://github.com/remkop/picocli/releases/download/v4.7.7/$JAR_NAME"

echo "Downloading Picocli CLI tool..."
curl -L "$URL" -o "$BIN_DIR/$JAR_NAME"

echo "✅ Downloaded to $BIN_DIR/$JAR_NAME"
echo "👉 Run with: java -jar $BIN_DIR/$JAR_NAME"
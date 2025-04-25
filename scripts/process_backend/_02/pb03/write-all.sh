#!/bin/bash
set -e

lines=${1:-10000}
repeat=${2:-1}
file=${3:-output.csv}

echo "📝 Writing file: $file"
echo "📄 Lines: $lines"
echo "🔁 Repeat: $repeat"
echo "-----------------------------"

echo ""
echo "🐍 Python"
(cd python && bash write_file.sh "$lines" "$repeat" "../$file")

echo ""
echo "☕ Java"
(cd java && bash write_file.sh "$lines" "$repeat" "../$file")

echo ""
echo "🟢 NodeJS"
(cd node && bash write_file.sh "$lines" "$repeat" "../$file")

echo ""
echo "💠 C++"
(cd cpp && bash write_file.sh "$lines" "$repeat" "../$file")
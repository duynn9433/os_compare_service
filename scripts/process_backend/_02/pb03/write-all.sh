#!/bin/bash
set -e

lines=${1:-10000}
repeat=${2:-1}
file=${3:-output.csv}

echo "📝 Writing file: $file"
echo "📄 Lines: $lines"
echo "🔁 Repeat: $repeat"
echo "-----------------------------"

total_start=$(date +%s%3N)
sleep 60
echo ""
echo "🐍 Python"
python_start=$(date +%s%3N)
(cd python && bash write_file.sh "$lines" "$repeat" "../$file")
python_end=$(date +%s%3N)
python_elapsed=$(( python_end - python_start ))
sleep 60
echo ""
echo "☕ Java"
java_start=$(date +%s%3N)
(cd java && bash write_file.sh "$lines" "$repeat" "../$file")
java_end=$(date +%s%3N)
java_elapsed=$(( java_end - java_start ))
sleep 60
echo ""
echo "🟢 NodeJS"
node_start=$(date +%s%3N)
(cd node && bash write_file.sh "$lines" "$repeat" "../$file")
node_end=$(date +%s%3N)
node_elapsed=$(( node_end - node_start ))
sleep 60
echo ""
echo "💠 C++"
cpp_start=$(date +%s%3N)
(cd cpp && bash write_file.sh "$lines" "$repeat" "../$file")
cpp_end=$(date +%s%3N)
cpp_elapsed=$(( cpp_end - cpp_start ))

total_end=$(date +%s%3N)
total_elapsed=$(( total_end - total_start ))

echo ""
echo "============================="
echo "🏁 Tổng kết thời gian:"
echo "🐍 Python: ${python_elapsed} ms"
echo "☕ Java: ${java_elapsed} ms"
echo "🟢 NodeJS: ${node_elapsed} ms"
echo "💠 C++: ${cpp_elapsed} ms"
echo "-----------------------------"
echo "⏱️  Tổng thời gian toàn bài: ${total_elapsed} ms"
echo "============================="
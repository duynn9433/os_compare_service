#!/bin/bash
set -e
n=${1:-1}

# Array to hold timing results
times=()

echo "⚙️ Building all languages..."
echo "---------------------------"

(cd c++ && bash build.sh)
(cd java && bash build.sh)

echo ""
echo "🚀 Running all Fibonacci benchmarks $n time(s)"
echo "----------------------------------------------"

measure_time() {
  lang=$1
  path=$2
  script=$3

  echo "====== $lang ======"
  start=$(date +%s.%N)
  (cd "$path" && bash "$script" "$n")
  end=$(date +%s.%N)

  duration=$(echo "$end - $start" | bc)
  times+=("$lang,$duration")
  echo "🕒 $lang total time (only run): $duration seconds"
}

sleep 10
measure_time "C++"     "c++"     "fib.sh"
sleep 10
measure_time "Java"    "java"    "fib.sh"
sleep 10
measure_time "Python"  "python"  "fib.sh"
sleep 10
measure_time "NodeJS"  "node"    "fib.sh"

# Print summary at the end
echo ""
echo "📊 Summary Table (for $n run(s))"
echo "--------------------------------"
echo "Language   | Total Time (s)"
echo "-----------|----------------"
for result in "${times[@]}"; do
  lang=$(echo $result | cut -d, -f1)
  duration=$(echo $result | cut -d, -f2)
  echo "$lang     | $duration seconds"
done
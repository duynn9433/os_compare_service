#!/bin/bash
set -e
n=${1:-1}

for ((i = 1; i <= n; i++)); do
    echo "🔁 C++ Run $i"
    ./fib_benchmark
done
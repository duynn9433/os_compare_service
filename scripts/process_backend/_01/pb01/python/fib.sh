#!/bin/bash
set -e
n=${1:-1}
for ((i = 1; i <= n; i++)); do
    echo "🔁 Python Run $i"
    python3 fib.py
done
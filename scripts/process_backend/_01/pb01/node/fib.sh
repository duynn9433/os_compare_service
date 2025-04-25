#!/bin/bash
set -e
n=${1:-1}
for ((i = 1; i <= n; i++)); do
    echo "🔁 NodeJS Run $i"
    node fib.js
done
#!/bin/bash
set -e
n=${1:-1}
for ((i = 1; i <= n; i++)); do
    echo "🔁 Matrix Multiply Run $i"
    sudo python3 matrix-mul.py
done
#!/bin/bash
set -e
clang++ -O2 fib.cpp -o fib_benchmark
echo "✅ C++ Build complete."
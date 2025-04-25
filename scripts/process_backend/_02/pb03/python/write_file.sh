#!/bin/bash
set -e

lines=${1:-10000}
repeat=${2:-1}
file=${3:-../output.csv}

for ((i = 1; i <= repeat; i++)); do
  echo "Run #$i"
  python3 write_file.py "$lines" "$file"
done
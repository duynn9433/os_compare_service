#!/bin/bash
set -e

lines=${1:-10000000}
repeat=${2:-1}
file=${3:-../output.csv}

for ((i = 1; i <= repeat; i++)); do
  echo "Run #$i"
  node write_file.js "$lines" "$file"
done
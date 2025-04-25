#!/bin/bash
set -e

lines=${1:-10000}
repeat=${2:-1}
file=${3:-../output.csv}

clang++ -O2 write_file.cpp -o write_file

for ((i = 1; i <= repeat; i++)); do
  echo "Run #$i"
  ./write_file "$lines" "$file"
done
#!/bin/bash
set -e

file=${1:-../sample.csv}
repeat=${2:-1}

for ((i = 1; i <= repeat; i++)); do
  echo "Run #$i"
  java ReadFile "$file"
done
#!/bin/bash
set -e

lines=${1:-100000000}
repeat=${2:-1}
file=${3:-../output.csv}

if [ ! -f WriteFile.class ]; then
  javac WriteFile.java
fi

for ((i = 1; i <= repeat; i++)); do
  echo "Run #$i"
  java WriteFile "$lines" "$file"
done
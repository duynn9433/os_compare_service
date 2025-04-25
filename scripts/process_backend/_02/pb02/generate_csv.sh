#!/bin/bash
# generate_csv.sh

n=${1:-10000}
echo "Generating sample.csv with $n lines..." > sample.csv
for ((i = 1; i <= n; i++)); do
    echo "Line $i" >> sample.csv
done
echo "✅ Done."
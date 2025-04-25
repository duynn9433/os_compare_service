#!/bin/bash

# Usage: ./run-wrk.sh <host>
# Example: ./run-wrk.sh 192.168.1.100

HOST="$1"

if [ -z "$HOST" ]; then
  echo "Usage: $0 <host>"
  exit 1
fi

wrk -t 1 -c 1 -d 10s -s RD02-write.lua --timeout 300s "http://$HOST:8080"
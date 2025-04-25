HOST="$1"

if [ -z "$HOST" ]; then
  echo "Usage: $0 <host>"
  exit 1
fi

wrk -t 1 -c 1 -d 10s -s RD01-read.lua --timeout 300s "http://$HOST:8080"
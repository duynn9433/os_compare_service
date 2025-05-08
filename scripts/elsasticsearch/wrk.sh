
HOST="$1"
WRK_TEST_NAME="$2"

if [ -z "$HOST" ] || [ -z "$WRK_TEST_NAME" ]; then
  echo "Usage: $0 <host> <wrk_test_name>"
  exit 1
fi

export WRK_TEST_NAME="$WRK_TEST_NAME"

wrk -t4 -c100 -d5s -s query.lua http://$HOST:9200/test-index/_search
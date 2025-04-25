#!/bin/bash

HOST="$1"
N="$2"

if [ -z "$HOST" ] || [ -z "$N" ]; then
  echo "Usage: $0 <host> <number_of_requests>"
  exit 1
fi

TOTAL_UPLOAD=0

START_TIME=$(date +%s.%N)

for ((i = 1; i <= N; i++)); do
  OUTPUT=$(curl -s -o /dev/null http://$HOST:8080/rdb/write-mariadb/update \
    -X PUT --data "@RD03-update.json" \
    -H "Content-Type: application/json" \
    -w "%{size_upload}")

  SIZE_UPLOAD=$(echo $OUTPUT | awk '{print $1}')
  TOTAL_UPLOAD=$(echo "$TOTAL_UPLOAD + $SIZE_UPLOAD" | bc)
done

END_TIME=$(date +%s.%N)
DURATION=$(echo "$END_TIME - $START_TIME" | bc)

# Convert bytes to MB (1 MB = 1024 * 1024 bytes)
TOTAL_UPLOAD_MB=$(echo "scale=4; $TOTAL_UPLOAD / 1048576" | bc)
AVG_SPEED_MBPS=$(echo "scale=4; $TOTAL_UPLOAD_MB / $DURATION" | bc)
REQ_PER_SEC=$(echo "scale=2; $N / $DURATION" | bc)

echo "📈 KẾT QUẢ TỔNG HỢP"
echo "⏱️  Tổng thời gian thực hiện: ${DURATION}s"
echo "📤 Tổng dữ liệu đã gửi: ${TOTAL_UPLOAD_MB} MB"
echo "⚡ Tốc độ upload trung bình: ${AVG_SPEED_MBPS} MB/s"
echo "📬 Số request/giây: ${REQ_PER_SEC} req/s"
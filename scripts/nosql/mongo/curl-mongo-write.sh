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
  OUTPUT=$(curl -s -o /dev/null http://$HOST:8080/nosql/mongo \
    -X POST --data "@mongo-100000.json" \
    -H "Content-Type: application/json" \
    -w "%{size_upload}")

  SIZE_UPLOAD=$(echo $OUTPUT | awk '{print $1}')
  TOTAL_UPLOAD=$(echo "$TOTAL_UPLOAD + $SIZE_UPLOAD" | bc)
done

END_TIME=$(date +%s.%N)
DURATION=$(echo "$END_TIME - $START_TIME" | bc -l)

# Convert bytes to MB (1 MB = 1024 * 1024 bytes)
TOTAL_UPLOAD_MB=$(echo "$TOTAL_UPLOAD / 1048576" | bc -l)
AVG_SPEED_MBPS=$(echo "$TOTAL_UPLOAD_MB / $DURATION" | bc -l)
REQ_PER_SEC=$(echo "$N / $DURATION" | bc -l)

echo "📈 KẾT QUẢ TỔNG HỢP"
printf "⏱️  Tổng thời gian thực hiện: %.4f s\n" "$DURATION"
printf "📤 Tổng dữ liệu đã gửi: %.4f MB\n" "$TOTAL_UPLOAD_MB"
printf "⚡ Tốc độ upload trung bình: %.4f MB/s\n" "$AVG_SPEED_MBPS"
printf "📬 Số request/giây: %.4f req/s\n" "$REQ_PER_SEC"
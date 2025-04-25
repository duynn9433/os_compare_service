#!/bin/bash

HOST="$1"
N="$2"
FILE="file.bin"

if [ -z "$HOST" ] || [ -z "$N" ]; then
  echo "Usage: $0 <host> <number_of_requests>"
  exit 1
fi

if [ ! -f "$FILE" ]; then
  echo "❌ File '$FILE' không tồn tại!"
  exit 2
fi

FILE_SIZE=$(stat --printf="%s" "$FILE")
TOTAL_UPLOAD=0

START_TIME=$(date +%s.%N)

for ((i = 1; i <= N; i++)); do
  curl -s -o /dev/null -X POST "http://$HOST:8080/wsb/file" \
    -F "file=@$FILE" \
    -w ""
  TOTAL_UPLOAD=$(echo "$TOTAL_UPLOAD + $FILE_SIZE" | bc)
done

END_TIME=$(date +%s.%N)
DURATION=$(echo "$END_TIME - $START_TIME" | bc -l)

# Convert to MB
TOTAL_UPLOAD_MB=$(echo "$TOTAL_UPLOAD / 1048576" | bc -l)
AVG_SPEED_MBPS=$(echo "$TOTAL_UPLOAD_MB / $DURATION" | bc -l)
REQ_PER_SEC=$(echo "$N / $DURATION" | bc -l)

echo "📈 KẾT QUẢ TỔNG HỢP"
printf "⏱️  Tổng thời gian thực hiện: %.4f s\n" "$DURATION"
printf "📤 Tổng dữ liệu đã gửi: %.4f MB\n" "$TOTAL_UPLOAD_MB"
printf "⚡ Tốc độ upload trung bình: %.4f MB/s\n" "$AVG_SPEED_MBPS"
printf "📬 Số request/giây: %.4f req/s\n" "$REQ_PER_SEC"
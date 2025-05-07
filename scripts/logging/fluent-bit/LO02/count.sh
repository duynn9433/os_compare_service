#!/bin/bash

OUT_FILE="received_logs.txt"
TARGET_INCREMENT=60000000  # Số log cần nhận thêm
INTERVAL=1  # Giây, khoảng thời gian giữa các lần kiểm tra

# Đợi cho đến khi file xuất hiện
while [ ! -f "$OUT_FILE" ]; do
    echo "⏳ Waiting for $OUT_FILE to be created..."
    sleep 1
done

echo "🚀 Waiting for $TARGET_INCREMENT new logs..."

initial_count=$(wc -l < "$OUT_FILE")
start_time=0
started=false

while true; do
    current_count=$(wc -l < "$OUT_FILE")
    received=$((current_count - initial_count))

    if [ "$received" -gt 0 ] && [ "$started" = false ]; then
        start_time=$(date +%s)
        started=true
        echo "⏱️ Started counting at $(date '+%H:%M:%S')"
    fi

    if [ "$received" -ge "$TARGET_INCREMENT" ]; then
        end_time=$(date +%s)
        duration=$((end_time - start_time))
        size_bytes=$(stat --format=%s "$OUT_FILE")
        size_mb=$(echo "scale=2; $size_bytes / 1024 / 1024" | bc)

        echo "✅ Received $received new log lines"
        echo "⏱️ Duration: $duration seconds"
        echo "📦 Throughput: $(echo "scale=2; $size_mb / $duration" | bc) MB/s"
        exit 0
    fi

    sleep $INTERVAL
done
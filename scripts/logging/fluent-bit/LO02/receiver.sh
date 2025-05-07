#!/bin/bash

PORT=5170
OUT_FILE="received_logs.txt"

echo "🟢 Listening on port $PORT and writing to $OUT_FILE..."

# Tạo file nếu chưa tồn tại
touch "$OUT_FILE"

# Lắng nghe vĩnh viễn trên cổng 5170 và ghi vào file (append)
while true; do
    nc -lk $PORT >> "$OUT_FILE"
done
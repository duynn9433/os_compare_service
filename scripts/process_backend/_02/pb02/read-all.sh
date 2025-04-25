#!/bin/bash
set -e

file=${1:-sample.csv}      # Đường dẫn file dữ liệu, mặc định sample.csv
repeat=${2:-1}             # Số lần chạy, mặc định 1

# Mảng để lưu thời gian chạy cho từng ngôn ngữ
times_python=0
times_java=0
times_node=0
times_cpp=0

bash generate_csv.sh
(cd java && bash build.sh)
(cd cpp && bash build.sh)

echo "📂 Reading file: $file"
echo "🔁 Repeat: $repeat"
echo "-----------------------------"

# Hàm đo thời gian và chạy script
measure_time() {
  lang=$1
  path=$2
  script=$3
  result_var=$4

  # Ghi nhận thời gian bắt đầu
  start_time=$(date +%s.%N)

  # Chạy script
  (cd "$path" && bash "$script" "../$file" "$repeat")

  # Ghi nhận thời gian kết thúc
  end_time=$(date +%s.%N)

  # Tính thời gian chạy (real time)
  duration=$(echo "$end_time - $start_time" | bc)

  # Lưu thời gian vào biến tương ứng
  eval "$result_var=$duration"
}

# Chạy và đo thời gian cho từng ngôn ngữ
measure_time "🐍 Python" "python" "read.sh" times_python
measure_time "☕ Java" "java" "read.sh" times_java
measure_time "🟢 NodeJS" "node" "read.sh" times_node
measure_time "💠 C++" "cpp" "read.sh" times_cpp

# In bảng tổng hợp thời gian ở cuối
echo ""
echo "📊 Summary Table (for $repeat run(s))"
echo "------------------------------------"
echo "Language   | Total Time (s)"
echo "-----------|----------------"
echo "🐍 Python   | $times_python seconds"
echo "☕ Java     | $times_java seconds"
echo "🟢 NodeJS   | $times_node seconds"
echo "💠 C++      | $times_cpp seconds"
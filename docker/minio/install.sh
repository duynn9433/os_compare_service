#!/bin/bash
set -e

echo "🔧 [1] Khởi tạo môi trường Python..."

# Tạo và kích hoạt virtualenv
if [ ! -d "venv" ]; then
    python3 -m venv venv
fi
source venv/bin/activate
pip install --upgrade pip
pip install boto3

echo "✅ Môi trường Python đã sẵn sàng."

echo "🔧 [2] Cài đặt MinIO Client (mc) thông qua package manager..."

install_mc() {
  if command -v mc &>/dev/null; then
    echo "✅ mc đã được cài sẵn."
    return
  fi

  OS=$(uname)
  if [[ "$OS" == "Linux" ]]; then
    curl https://dl.min.io/client/mc/release/linux-amd64/mc \
      --create-dirs \
      -o $HOME/minio-binaries/mc

    chmod +x $HOME/minio-binaries/mc
    export PATH=$PATH:$HOME/minio-binaries/
  elif [[ "$OS" == "Darwin" ]]; then
    echo "🍎 macOS detected. Cài qua Homebrew..."
    brew install minio/stable/mc || brew install mc
  else
    echo "❌ Không hỗ trợ hệ điều hành này: $OS"
    exit 1
  fi
}

install_mc

# 3. Tạo file 10GB (nếu chưa có)
echo "🗃️ [3] Tạo file 10GB (dd)..."
if [ ! -f "10GBfile" ]; then
  dd if=/dev/zero of=10GBfile bs=1M count=10240
else
  echo "📁 File 10GBfile đã tồn tại."
fi

# 4. Tạo alias và bucket
echo "🔧 [4] Kết nối MinIO và tạo bucket..."
mc alias set local http://localhost:9000 minioadmin minioadmin || true
mc mb --ignore-existing local/testbucket

echo "✅ Bucket testbucket đã sẵn sàng."

# 5. Hướng dẫn sử dụng
cat <<EOF

🎉 Môi trường đã sẵn sàng!

🔬 Chạy các bài test:

🔹 ST01 - Ghi dữ liệu:
    time mc cp 10GBfile local/testbucket

🔹 ST02 - Đọc dữ liệu:
    time mc cat local/testbucket/10GBfile > /dev/null

🔹 ST03 - Truy cập đồng thời:
    (Đảm bảo đã có file minio_upload.py)

    python3 minio-test.py --threads 10 --filesize 1024 --total 1000

EOF
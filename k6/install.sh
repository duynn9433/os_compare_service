#!/bin/bash

set -e

echo "=== [1] Kiểm tra OS ==="
OS=""
if [ -f /etc/os-release ]; then
    . /etc/os-release
    OS=$ID
else
    echo "Không xác định được OS, thoát!"
    exit 1
fi

echo "Hệ điều hành phát hiện: $OS"

echo "=== [2] Cài Go nếu cần ==="
if ! command -v go &> /dev/null; then
    echo "Go chưa cài. Tiến hành cài đặt Go."

    if [[ "$OS" == "ubuntu" ]]; then
        sudo apt remove golang-go
        sudo rm -rf /usr/local/go
    elif [[ "$OS" == "centos" ]]; then
        sudo yum remove golang
        sudo rm -rf /usr/local/go
    else
        echo "OS chưa hỗ trợ tự động cài Go."
        exit 1
    fi
else
    echo "Go đã được cài đặt."
fi

GO_VERSION=1.24.2
wget https://go.dev/dl/go${GO_VERSION}.linux-amd64.tar.gz
sudo tar -C /usr/local -xzf go${GO_VERSION}.linux-amd64.tar.gz
export PATH=$PATH:/usr/local/go/bin
source ~/.bashrc
echo "Go version: $(go version)"

echo "=== [3] Cài xk6 ==="


# Đảm bảo GOPATH có trong PATH
if [[ ":$PATH:" != *":$HOME/go/bin:"* ]]; then
    echo "Đang thêm ~/go/bin vào PATH tạm thời"
    export PATH=$PATH:$HOME/go/bin
fi

# Cảnh báo nếu người dùng muốn add vào ~/.bashrc hoặc ~/.zshrc
echo "Nếu muốn PATH tồn tại vĩnh viễn, hãy thêm dòng sau vào ~/.bashrc hoặc ~/.zshrc:"
echo 'export PATH=$PATH:$HOME/go/bin'

echo "=== [4] Build k6 + xk6-kafka ==="
xk6 build --with github.com/mostafa/xk6-kafka@latest
#--with github.com/dgzlopes/xk6-redis

echo "✅ Build thành công! File 'k6' đã được tạo."
echo "=> Bạn có thể chạy: ./k6 version"
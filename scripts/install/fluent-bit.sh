export http_proxy=192.168.2.205:3128
export https_proxy=192.168.2.205:3128
OS=$(uname)
if [[ "$OS" == "Linux" ]]; then
  curl https://raw.githubusercontent.com/fluent/fluent-bit/master/install.sh | sh
elif [[ "$OS" == "Darwin" ]]; then
  echo "🍎 macOS detected. Cài qua Homebrew..."
  brew install fluent-bit
else
  echo "❌ Không hỗ trợ hệ điều hành này: $OS"
  exit 1
fi
import boto3
import threading
import time
import argparse
import os
import shutil

def upload_file(i, file_size_kb, bucket_name, endpoint_url, access_key, secret_key, folder):
    try:
        filename = os.path.join(folder, f'tempfile_{i}.bin')
        # Tạo file nếu chưa tồn tại
        if not os.path.exists(filename):
            with open(filename, 'wb') as f:
                f.write(b'x' * 1024 * file_size_kb)

        s3 = boto3.client('s3', endpoint_url=endpoint_url,
                          aws_access_key_id=access_key,
                          aws_secret_access_key=secret_key)
        s3.upload_file(filename, bucket_name, os.path.basename(filename))
        print(f"✅ Uploaded {filename}")
    except Exception as e:
        print(f"❌ Error uploading file {i}: {e}")

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--threads', type=int, default=10)
    parser.add_argument('--filesize', type=int, default=1024)
    parser.add_argument('--total', type=int, default=100)
    parser.add_argument('--bucket', type=str, default='testbucket')
    parser.add_argument('--endpoint', type=str, default='http://localhost:9000')
    parser.add_argument('--access_key', type=str, default='minioadmin')
    parser.add_argument('--secret_key', type=str, default='minioadmin')
    args = parser.parse_args()

    os.makedirs('tempfiles', exist_ok=True)
    start = time.time()

    with ThreadPoolExecutor(max_workers=args.threads) as executor:
        futures = [
            executor.submit(upload_file, i, args.filesize, args.bucket, args.endpoint, args.access_key, args.secret_key)
            for i in range(args.total)
        ]
        for future in as_completed(futures):
            pass  # xử lý lỗi nếu muốn

    end = time.time()
    print(f"\n⏱️ Tổng thời gian upload: {end - start:.2f} giây")

    # Xoá thư mục chứa file tạm
    try:
        shutil.rmtree(args.folder)
        print(f"🧹 Đã xoá thư mục tạm: {args.folder}")
    except Exception as e:
        print(f"❌ Không thể xoá thư mục {args.folder}: {e}")

if __name__ == '__main__':
    main()
import boto3
from botocore.client import Config
from botocore.exceptions import ClientError

# Kết nối đến MinIO
s3 = boto3.client('s3',
    endpoint_url='http://localhost:9000',
    aws_access_key_id='minioadmin',
    aws_secret_access_key='minioadmin',
    config=Config(signature_version='s3v4'))

bucket_name = 'testbucket'

# Tạo bucket nếu chưa tồn tại
try:
    s3.head_bucket(Bucket=bucket_name)
    print(f"✅ Bucket '{bucket_name}' đã tồn tại")
except ClientError:
    print(f"ℹ️ Bucket '{bucket_name}' chưa tồn tại. Đang tạo...")
    s3.create_bucket(Bucket=bucket_name)
    print(f"✅ Đã tạo bucket '{bucket_name}'")

# Tạo và upload file
with open('testfile.bin', 'wb') as f:
    f.write(b'x' * 1024 * 1024)  # 1MB

s3.upload_file('testfile.bin', bucket_name, 'testfile.bin')
print("✅ Upload thành công")

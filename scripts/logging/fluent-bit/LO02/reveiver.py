import socket
from datetime import datetime

HOST = '0.0.0.0'
PORT = 5170
LOG_FILE = '/var/log/received.log'
TARGET_LINES = 10000

count = 0
start_time = None

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen(1)
    print(f"Listening on {PORT}...")
    conn, addr = s.accept()
    with conn:
        print(f"Connection from {addr}")
        with open(LOG_FILE, "w") as f:
            while True:
                data = conn.recv(4096)
                if not data:
                    break
                lines = data.decode(errors='ignore').splitlines()
                for line in lines:
                    f.write(line + "\n")
                    count += 1
                    if count == 1:
                        start_time = datetime.now()
                    if count >= TARGET_LINES:
                        end_time = datetime.now()
                        delta = (end_time - start_time).total_seconds()
                        print(f"Nhận đủ {TARGET_LINES} log trong {delta:.2f} giây")
                        exit(0)
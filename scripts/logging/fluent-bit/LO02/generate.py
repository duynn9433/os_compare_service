import time
from datetime import datetime

log_file = "/var/log/test_metrics.log"
total_logs = 60000000
interval_sec = 0.001  # 1ms giữa 2 log (~1000 logs/s)

start = time.time()

with open(log_file, "a") as f:
    for i in range(total_logs):
        now = datetime.now().isoformat()
        f.write(f"{now} | index={i}\n")
        #time.sleep(interval_sec)

end = time.time()
print(f"Ghi {total_logs} log trong {round(end - start, 2)} giây.")
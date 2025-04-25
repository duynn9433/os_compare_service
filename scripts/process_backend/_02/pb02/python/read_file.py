import csv
import time
import sys

filename = sys.argv[1] if len(sys.argv) > 1 else "sample.csv"

start = time.time()

with open(filename, newline='') as f:
    reader = csv.reader(f)
    rows = list(reader)

end = time.time()

print(f"✅ Python read {len(rows)} rows in {end - start:.4f} seconds")
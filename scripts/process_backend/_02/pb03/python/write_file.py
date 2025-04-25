import sys
import time

lines = int(sys.argv[1]) if len(sys.argv) > 1 else 10000
filepath = sys.argv[2] if len(sys.argv) > 2 else 'output.csv'

start = time.time()
with open(filepath, 'w') as f:
    for i in range(lines):
        f.write(f"Line {i+1},Some data here\n")
end = time.time()

print(f"[Python] Wrote {lines} lines to {filepath} in {end - start:.4f} seconds.")
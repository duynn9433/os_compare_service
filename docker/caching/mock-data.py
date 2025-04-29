import redis
import hazelcast
import random
import string
from time import time

NUM_KEYS = 100_000
VALUE_LENGTH = 256

def random_value(length=256):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def redis_test():
    print("📦 Redis:")
    r = redis.Redis(host="localhost", port=6379)

    initial_count = r.dbsize()
    print(f"  🔍 Keys before: {initial_count}")

    keys_to_add = NUM_KEYS - initial_count
    if keys_to_add <= 0:
        print("  ⚠️ No need to add more keys.")
        return

    start_index = initial_count
    start = time()
    for i in range(start_index, start_index + keys_to_add):
        key = f"key:{i}"
        value = random_value(VALUE_LENGTH)
        r.set(key, value)
    duration = time() - start

    final_count = r.dbsize()
    print(f"  ✅ Keys after: {final_count} (Added: {final_count - initial_count})")
    print(f"  ⏱️ Time to insert {keys_to_add} keys: {duration:.2f}s\n")

def hazelcast_test():
    print("📦 Hazelcast:")
    client = hazelcast.HazelcastClient(cluster_members=["localhost:5701"])
    test_map = client.get_map("test-map").blocking()

    initial_count = test_map.size()
    print(f"  🔍 Keys before: {initial_count}")

    keys_to_add = NUM_KEYS - initial_count
    if keys_to_add <= 0:
        print("  ⚠️ No need to add more keys.")
        client.shutdown()
        return

    start_index = initial_count
    start = time()
    for i in range(start_index, start_index + keys_to_add):
        key = f"key:{i}"
        value = random_value(VALUE_LENGTH)
        test_map.put(key, value)
    duration = time() - start

    final_count = test_map.size()
    print(f"  ✅ Keys after: {final_count} (Added: {final_count - initial_count})")
    print(f"  ⏱️ Time to insert {keys_to_add} keys: {duration:.2f}s\n")

    client.shutdown()

if __name__ == "__main__":
    redis_test()
    hazelcast_test()
import redis
import hazelcast

def test_redis():
    print("🔧 Testing Redis...")
    r = redis.Redis(host="localhost", port=6379)
    r.set("test_key", "redis_ok")
    value = r.get("test_key")
    print("✅ Redis response:", value.decode())

def test_hazelcast():
    print("🔧 Testing Hazelcast...")
    client = hazelcast.HazelcastClient(cluster_members=["localhost:5701"])
    test_map = client.get_map("test-map").blocking()
    test_map.put("test_key", "hazelcast_ok")
    value = test_map.get("test_key")
    print("✅ Hazelcast response:", value)
    client.shutdown()

if __name__ == "__main__":
    test_redis()
    test_hazelcast()
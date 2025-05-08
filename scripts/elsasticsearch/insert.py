import requests
import json
import time
from tqdm import tqdm
from datetime import datetime

ES_HOST = "http://localhost:9200"
INDEX_NAME = "test-index"
DATA_FILE = "data.jsonl"
BULK_SIZE = 500  # Số lượng document mỗi lần ghi bulk
HEADERS = {"Content-Type": "application/x-ndjson"}

# Lấy tổng số document trong index
def get_doc_count():
    url = f"{ES_HOST}/{INDEX_NAME}/_count"
    response = requests.get(url)
    if response.status_code == 200:
        count = response.json().get("count", 0)
        print(f"📦 Total documents in '{INDEX_NAME}': {count}")
        return count
    else:
        print(f"❌ Failed to get document count: {response.text}")
        return -1

# Gửi bulk request đến Elasticsearch
def bulk_insert(docs):
    bulk_payload = ""
    for doc in docs:
        # Không dùng _id → tránh overwrite
        meta = {"index": {"_index": INDEX_NAME}}
        bulk_payload += json.dumps(meta) + "\n" + json.dumps(doc) + "\n"
    response = requests.post(f"{ES_HOST}/_bulk", headers=HEADERS, data=bulk_payload)
    return response

# Ghi dữ liệu từ file vào Elasticsearch
def ingest_documents(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        batch = []
        total = 0
        errors = 0
        start_time = time.time()

        for line in tqdm(f, desc="Processing JSONL"):
            doc = json.loads(line)
            batch.append(doc)
            if len(batch) >= BULK_SIZE:
                resp = bulk_insert(batch)
                total += len(batch)
                if resp.status_code != 200 or resp.json().get("errors", False):
                    print(f"❌ Bulk insert failed: {resp.text}")
                    errors += 1
                batch = []

        if batch:
            resp = bulk_insert(batch)
            total += len(batch)
            if resp.status_code != 200 or resp.json().get("errors", False):
                print(f"❌ Final bulk insert failed: {resp.text}")
                errors += 1

        # Buộc Elasticsearch refresh index để count chính xác ngay sau khi ghi
        requests.post(f"{ES_HOST}/{INDEX_NAME}/_refresh")

        end_time = time.time()
        elapsed = end_time - start_time

        print("\n=== 📊 Ingestion Report ===")
        print(f"🕒 Time taken     : {elapsed:.2f} seconds")
        print(f"📄 Total inserted: {total} documents")
        print(f"⚠️  Bulk errors  : {errors} chunks")
        print(f"🚀 Insert speed  : {total / elapsed:.2f} docs/sec")
        return elapsed, total, errors

if __name__ == "__main__":
    print(datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    print("🔍 Counting documents before insert...")
    count_before = get_doc_count()

    print("🚀 Inserting documents from file...")
    duration, inserted, error_batches = ingest_documents(DATA_FILE)

    print("🔍 Counting documents after insert...")
    count_after = get_doc_count()

    print(f"✅ Documents after: {count_after}")
    print(f"📊 Documents added: {count_after - count_before}")
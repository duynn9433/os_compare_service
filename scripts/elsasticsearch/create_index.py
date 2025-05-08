import requests
import json

# Cấu hình Elasticsearch
ES_HOST = "http://localhost:9200"
INDEX_NAME = "test-index"

# 1. Tạo index với mapping
def create_index():
    url = f"{ES_HOST}/{INDEX_NAME}"
    mapping = {
        "mappings": {
            "properties": {
                "id": {"type": "integer"},
                "title": {"type": "text"},
                "content": {"type": "text"},
                "author": {"type": "keyword"},
                "timestamp": {"type": "date"},
                "category": {"type": "keyword"},
                "tags": {"type": "keyword"},
                "score": {"type": "float"}
            }
        }
    }
    response = requests.put(url, headers={"Content-Type": "application/json"}, data=json.dumps(mapping))
    print(f"[+] Create index: {response.status_code}, {response.text}")

# 2. Ghi một document mẫu
def index_document():
    doc = {
        "id": 1,
        "title": "Test Document",
        "content": "This is a sample document for testing.",
        "author": "Alice",
        "timestamp": "2024-06-01T00:00:00",
        "category": "tech",
        "tags": ["AI", "ML"],
        "score": 0.85
    }
    url = f"{ES_HOST}/{INDEX_NAME}/_doc/{doc['id']}"
    response = requests.put(url, headers={"Content-Type": "application/json"}, data=json.dumps(doc))
    print(f"[+] Index document: {response.status_code}, {response.text}")

# 3. Đọc lại document theo ID
def get_document(doc_id=1):
    url = f"{ES_HOST}/{INDEX_NAME}/_doc/{doc_id}"
    response = requests.get(url)
    print(f"[+] Get document: {response.status_code}")
    print(json.dumps(response.json(), indent=2))

if __name__ == "__main__":
    create_index()
    index_document()
    get_document()
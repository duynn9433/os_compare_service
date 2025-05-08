import requests
import argparse

ES_HOST = "http://localhost:9200"
INDEX_NAME = "test-index"

def get_doc_count():
    url = f"{ES_HOST}/{INDEX_NAME}/_count"
    response = requests.get(url)
    if response.status_code == 200:
        count = response.json().get("count", 0)
        print(f"📦 Total documents in '{INDEX_NAME}': {count}")
    else:
        print(f"❌ Failed to get document count: {response.text}")

def delete_all_documents():
    url = f"{ES_HOST}/{INDEX_NAME}/_delete_by_query"
    query = {
        "query": {
            "match_all": {}
        }
    }
    response = requests.post(url, headers={"Content-Type": "application/json"}, json=query)
    if response.status_code == 200:
        deleted = response.json().get("deleted", 0)
        print(f"🧹 Deleted {deleted} documents from '{INDEX_NAME}'")
    else:
        print(f"❌ Failed to delete documents: {response.text}")

def delete_index():
    url = f"{ES_HOST}/{INDEX_NAME}"
    response = requests.delete(url)
    if response.status_code == 200:
        print(f"🗑️ Index '{INDEX_NAME}' deleted successfully.")
    elif response.status_code == 404:
        print(f"ℹ️ Index '{INDEX_NAME}' does not exist.")
    else:
        print(f"❌ Failed to delete index: {response.text}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Elasticsearch index utility.")
    parser.add_argument("--count", action="store_true", help="Get document count")
    parser.add_argument("--delete-docs", action="store_true", help="Delete all documents")
    parser.add_argument("--delete-index", action="store_true", help="Delete entire index")
    args = parser.parse_args()

    if args.count:
        get_doc_count()
    if args.delete_docs:
        delete_all_documents()
    if args.delete_index:
        delete_index()
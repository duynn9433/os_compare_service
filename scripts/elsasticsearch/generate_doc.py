import json
import random
from datetime import datetime, timedelta
import argparse

def generate_documents(num_docs, output_file):
    authors = ["Alice", "Bob", "Charlie", "Diana", "Eve"]
    categories = ["tech", "news", "sports", "health", "finance"]
    all_tags = ["AI", "ML", "Blockchain", "COVID", "Stock", "Football", "Nutrition", "Startup"]
    base_date = datetime(2024, 1, 1)

    with open(output_file, 'w', encoding='utf-8') as f:
        for i in range(1, num_docs + 1):
            doc = {
                "id": i,
                "title": f"Document {i}",
                "content": f"This is the content of document {i}.",
                "author": random.choice(authors),
                "timestamp": (base_date + timedelta(days=random.randint(0, 365))).isoformat(),
                "category": random.choice(categories),
                "tags": random.sample(all_tags, k=random.randint(1, 3)),
                "score": round(random.uniform(0.0, 1.0), 3)
            }
            f.write(json.dumps(doc) + "\n")

    print(f"✅ Generated {num_docs} documents with advanced fields in {output_file}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Generate advanced JSONL documents.")
    parser.add_argument("-n", "--num", type=int, default=1000, help="Number of documents to generate")
    parser.add_argument("-o", "--output", type=str, default="data.jsonl", help="Output JSONL file name")
    args = parser.parse_args()

    generate_documents(args.num, args.output)
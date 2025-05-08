curl -X POST "localhost:9200/test-index/_search" \
     -H 'Content-Type: application/json' \
     -d @query.json
#!/bin/bash

echo ">>> Waiting for Kafka to be ready..."
while ! nc -z localhost 9092; do
  sleep 1
done
echo ">>> Kafka is up."

TOPIC_NAME="test-topic"

if /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list | grep -wq "$TOPIC_NAME"; then
  echo ">>> Topic '$TOPIC_NAME' already exists."
else
  echo ">>> Creating topic: $TOPIC_NAME"
  /opt/kafka/bin/kafka-topics.sh --create \
    --bootstrap-server localhost:9092 \
    --topic "$TOPIC_NAME" \
    --partitions 1 \
    --replication-factor 1
fi

echo ">>> Done creating topic."
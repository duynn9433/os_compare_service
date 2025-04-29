docker exec -it kafka-kafka-1 /opt/kafka/bin/kafka-topics.sh --create \
--bootstrap-server localhost:9092 \
--topic test \
--partitions 3 \
--replication-factor 1
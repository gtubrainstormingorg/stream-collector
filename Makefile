.PHONY: all integration-up


integration-up:
	(cd integration && docker-compose -f ./docker-compose.yml up -d)
	sleep 60
	echo "waiting for kafka to be ready"
	docker exec broker cub kafka-ready -b broker:9092 1 120
	docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic good && \
	docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic bad

integration-success-output:
	docker exec broker kafka-consumer --topic=good --from-beginning --bootstrap-server broker:9092

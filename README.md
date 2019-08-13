# Kafka 

![enter image description here](https://github.com/kuldeepsingh82/Kafka/blob/master/images/kafka.jpg)


## Console Commands

### Start zookeeper
	
	$ ./bin/zookeeper-server-start.sh config/zookeeper.properties

### Start Kafka broker

	$ ./bin/kafka-server-start.sh config/server.properties

### Start multiple Kafka brokers

Copy config/server.properties to multiple configuration files. Change following three properties keys in the files
* broker.id={New Integer ID}
* listeners=PLAINTEXT://:{New Port}
* log.dirs=./data/{New Directory}

Now start multiple brokers each one pointing to one of these configuration files.

### Create a topic

	$ ./bin/kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic deep_topic_1 --partitions 3 --replication-factor 1

### List all topics

	$ ./bin/kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --list

### Describe a topic

	$ ./bin/kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --topic deep_topic_1 --describe

### Start a console producer

	$ ./bin/kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic deep_topic_1

### Start a console consumer

	$ ./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic deep_topic_1

### Start a console consumer (from beginning)

	$ ./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic deep_topic_1 --from-beginning

### Start a console consumer with consumer-group

	$ ./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic deep_topic_1 --group deep_app_1

### List all consumer groups

	$ ./bin/kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group --list

### Describe a consumer group

	$ ./bin/kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group deep_app_1 --describe

### Reset the offset of a consumer group for a topic

	$ ./bin/kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group deep_app_1 --topic deep_topic_1 --reset-offsets --to-earliest --execute

For more details check [Kafka Quick Start](https://kafka.apache.org/quickstart)

package org.kafka.java.producer.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class MyKafkaConsumer {

	private Consumer<String, String> kafkaConsumer = null;
	private final String kafkaServer = "127.0.0.1:9092";

	public MyKafkaConsumer() {

	}

	public void initializeKafkaConsumer(String topic) {

		// Create producer properties
		Properties kafkaProperties = new Properties();
		kafkaProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		kafkaProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "java-consumer-app-1");
		kafkaProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		kafkaProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		kafkaProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // earliest, latest, none

		// Create Kafka consumner
		Consumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(kafkaProperties);

		// Subscribe to topic(s)
		kafkaConsumer.subscribe(Collections.singletonList(topic));

		this.kafkaConsumer = kafkaConsumer;
	}

	public void receiveMessage(String topic) {
		if (this.kafkaConsumer == null) {
			System.out.println("Initializing Kafka Consumer...");
			this.initializeKafkaConsumer(topic);
		}

		while (true) {
			ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(200));
			for (ConsumerRecord<String, String> consumerRecord : records) {
				System.out.println("Received : \n" + "Topic : " + consumerRecord.topic() + ", " + "Message : "
						+ consumerRecord.value());
			}
		}
	}
}

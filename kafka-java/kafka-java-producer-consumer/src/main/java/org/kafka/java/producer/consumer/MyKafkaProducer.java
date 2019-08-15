package org.kafka.java.producer.consumer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyKafkaProducer {

	private Producer<String, String> kafkaProducer = null;
	private final String kafkaServer = "127.0.0.1:9092";

	public MyKafkaProducer() {

	}

	public void initializeKafkaProducer() {

		// Create Producer properties
		Properties kafkaProperties = new Properties();
		kafkaProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		kafkaProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProperties.setProperty(ProducerConfig.ACKS_CONFIG, "1");

		// Create Kafka producer
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaProperties);

		this.kafkaProducer = kafkaProducer;
	}

	public void sendMessage(String topic, String message) {
		if (this.kafkaProducer == null) {
			System.out.println("Initializing Kafka Producer...");
			this.initializeKafkaProducer();
		}

		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);

		// Send message
		System.out.println("Sending message asynchronously...");
		this.kafkaProducer.send(record);

	}

	public void closeProducer() {
		// Flush the producer OR Flush & Close the producer
		// this.kafkaProducer.flush();
		this.kafkaProducer.close();
	}

}

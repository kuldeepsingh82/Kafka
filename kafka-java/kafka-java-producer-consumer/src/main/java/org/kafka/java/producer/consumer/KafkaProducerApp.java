package org.kafka.java.producer.consumer;

public class KafkaProducerApp {

	public static void main(String[] args) {
		
		// Simple Kafka Producer
		new KafkaProducerApp().runProducer();
		
		// Kafka Producer with Callback
		new KafkaProducerApp().runProducerWithCallback();
		
		// Kafka Producer with a key (Will be sent to one partition)
		new KafkaProducerApp().runProducerWithKeys();
	}

	private void runProducer() {
		MyKafkaProducer producer = new MyKafkaProducer();

		String topic = "java-topic-1";
		for (int i = 0; i < 10; i++) {
			producer.sendMessage(topic, "Hello Kafka " + i);
		}
		producer.closeProducer();
	}
	
	private void runProducerWithCallback() {
		MyKafkaProducerWithCallback producer = new MyKafkaProducerWithCallback();

		String topic = "java-topic-1";
		for (int i = 0; i < 10; i++) {
			producer.sendMessage(topic, "Hello Kafka with Callback " + i);
		}
		producer.closeProducer();
	}
	
	private void runProducerWithKeys() {
		MyKafkaProducerWithKeys producer = new MyKafkaProducerWithKeys();
		final String topic = "java-topic-1";
		final String kafkaKey = "ABC123";
		for (int i = 0; i < 10; i++) {
			producer.sendMessage(topic, "Hello Kafka with Keys (Same Partition) " + i, kafkaKey);
		}
		producer.closeProducer();
	}
}

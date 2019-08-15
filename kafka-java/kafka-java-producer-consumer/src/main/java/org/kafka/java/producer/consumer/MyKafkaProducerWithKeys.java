package org.kafka.java.producer.consumer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyKafkaProducerWithKeys {

	private Producer<String, String> kafkaProducer = null;
	private final String kafkaServer = "127.0.0.1:9092";
	
	public MyKafkaProducerWithKeys() {

	}

	public Producer<String, String> initializeKafkaProducer() {
		
		// Create producer properties
		Properties kafkaProperties = new Properties();
		kafkaProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		kafkaProperties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
		kafkaProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// Create kafka producer
		Producer<String, String> kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(
				kafkaProperties);
		this.kafkaProducer = kafkaProducer;
		return kafkaProducer;
	}

	public void sendMessage(String topic, String message, String key) {
		if (this.kafkaProducer == null) {
			System.out.println("Initializing Kafka Producer...");
			this.initializeKafkaProducer();
		}

		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, message);
		System.out.println("Sending message...");
		this.kafkaProducer.send(producerRecord, new Callback() {
			
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if(exception != null) {
					System.err.println("Unable to send message to kafka");
				} else {
					System.out.println("Kafka message sent : Partition :"+metadata.partition()+", Offset :"+metadata.offset()+" Topic :"+metadata.topic()+" Timestamp : "+metadata.timestamp());
				}
			}
		});
	}

	public void closeProducer() {
		// Flush the producer OR Flush & Close the producer
		// this.kafkaProducer.flush();
		this.kafkaProducer.close();
	}
}

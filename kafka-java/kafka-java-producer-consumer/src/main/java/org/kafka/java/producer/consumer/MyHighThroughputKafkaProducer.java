package org.kafka.java.producer.consumer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyHighThroughputKafkaProducer {

	private Producer<String, String> kafkaProducer = null;
	private final String kafkaServer = "127.0.0.1:9092";

	public MyHighThroughputKafkaProducer() {

	}

	public Producer<String, String> initializeKafkaProducer() {
		
		// Create producer properties
		Properties kafkaProperties = new Properties();
		kafkaProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		kafkaProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// Safe Kafka Producer Configuration
		kafkaProperties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		kafkaProperties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		kafkaProperties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		kafkaProperties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

		// High throughput Producer configuration
		kafkaProperties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // gzip, lz4, snappy
		kafkaProperties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "10"); // 10 ms
		kafkaProperties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // 32KB
		
		// Create kafka producer
		Producer<String, String> kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(
				kafkaProperties);
		this.kafkaProducer = kafkaProducer;
		return kafkaProducer;
	}

	public void sendMessage(String topic, String message) {
		if (this.kafkaProducer == null) {
			System.out.println("Initializing Kafka Producer...");
			this.initializeKafkaProducer();
		}

		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
		System.out.println("Sending message...");
		this.kafkaProducer.send(producerRecord, new Callback() {
			
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if(exception != null) {
					System.err.println("Unable to send message to kafka");
				} else {
					System.out.println("Kafka message sent : \n"+ 
							"Partition : "+metadata.partition()+" \n"+
							"Offset : "+metadata.offset()+" \n"+
							"Topic :"+metadata.topic()+" \n"+
							"Timestamp : "+metadata.timestamp());
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

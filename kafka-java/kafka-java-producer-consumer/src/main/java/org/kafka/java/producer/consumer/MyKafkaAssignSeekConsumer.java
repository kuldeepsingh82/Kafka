package org.kafka.java.producer.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class MyKafkaAssignSeekConsumer {

	private Consumer<String, String> kafkaConsumer = null;
	private final String kafkaServer = "127.0.0.1:9092";
	private int specificPartition;
	private long specificOffset;
	private int nbRecordsToRead;
	
	public MyKafkaAssignSeekConsumer(int specificPartition, long specificOffset, int nbRecordsToRead) {
		this.specificPartition = specificPartition;
		this.specificOffset = specificOffset;
		this.nbRecordsToRead = nbRecordsToRead;
	}

	public void initializeKafkaConsumer(String topic) {

		// Create producer properties
		Properties kafkaProperties = new Properties();
		kafkaProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		kafkaProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		kafkaProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		kafkaProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // earliest, latest, none

		// Create Kafka consumner
		Consumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(kafkaProperties);

		// Assign the partition & offset to consumer
		TopicPartition topicPartition = new TopicPartition(topic, this.specificPartition);
		kafkaConsumer.assign(Arrays.asList(topicPartition));
		kafkaConsumer.seek(topicPartition, this.specificOffset);
		
		this.kafkaConsumer = kafkaConsumer;
	}

	public void receiveMessage(String topic) {
		if (this.kafkaConsumer == null) {
			System.out.println("Initializing Kafka Consumer...");
			this.initializeKafkaConsumer(topic);
		}
		int nbMessagesRead = 0;
		boolean keepRunning = true;
		while (keepRunning) {
			ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(200));
			for (ConsumerRecord<String, String> consumerRecord : records) {
				System.out.println("Received : \n" + "Topic : " + consumerRecord.topic() + ", " + "Message : "
						+ consumerRecord.value());
				nbMessagesRead++;
				if(nbMessagesRead >= this.nbRecordsToRead) {
					keepRunning = false;
					break;
				}
			}
			
		}
	}
}

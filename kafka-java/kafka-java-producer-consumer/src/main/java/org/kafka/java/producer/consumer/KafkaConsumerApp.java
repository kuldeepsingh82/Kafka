package org.kafka.java.producer.consumer;

import java.util.concurrent.CountDownLatch;

public class KafkaConsumerApp {

	public static void main(String[] args) {

		// Run simple consumer app
		// new KafkaConsumerApp().runConsumer();
		
		//OR 
		//new KafkaConsumerApp().runConsumerWithThread();
		
		//OR Read a specific partition & offset
		new KafkaConsumerApp().runConsumerWithAssignAndSeek();
	}
	
	private void runConsumer() {
		String topic = "java-topic-1";
		MyKafkaConsumer consumer = new MyKafkaConsumer();
		consumer.receiveMessage(topic);
	}
	
	
	private void runConsumerWithAssignAndSeek() {
		String topic = "java-topic-1";
		int specificPartition = 0;
		long specificOffset = 77l;
		int nbRecordsToRead = 5;
		MyKafkaAssignSeekConsumer consumer = new MyKafkaAssignSeekConsumer(specificPartition, specificOffset, nbRecordsToRead);
		consumer.receiveMessage(topic);
	}
	
	
	private void runConsumerWithThread() {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		String topic = "java-topic-1";
		Runnable consumerRunnable = new MyKafkaConsumerWithThread(latch, topic);
		Thread thread = new Thread(consumerRunnable);
		
		System.out.println("Starting the application...");
		thread.start();
	
		// create a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("Application stop application hook received. Shutting down application");
			((MyKafkaConsumerWithThread) consumerRunnable).shutdownApplication();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// To trigger shutdown hook ONLY in eclipse. Can be removed otherwise
		try {
			Thread.sleep(5000);
			System.exit(0);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

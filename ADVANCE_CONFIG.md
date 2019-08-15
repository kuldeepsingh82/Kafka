# Kafka Configuration

## 1. Safe Producer Configuration for Kafka

### a. Broker acknowledgement
 
	acks

Possible values :
**0** : Broker doesn't reply. **Very fast** but we **might loose data**
**1** : (default) Leader acknowledgement : **Safer** but still possibility of data loss as **replication is not guaranteed**.   
**'all'** : All in-sync broker acknowledgement : **Safest** but **slow**.

### b. Producer Retries

Producer retry settings if message sending fails

	retries

*Default value = 0 for Kafka <= 2.0*
*Default value = Max Integer Value for Kafka > 2.0*

Producer will wait for retry backoff time before retrying the messages

	retry.backoff.ms

*Default value = 100 ms*

Producer doesn't retry for max integer value if message sending/retry is failing, producer will stop after some time defined by 

	delivery.timeout.ms

*Default value = 120000 ms = 2 minutes*
After this time producer won't retry

If producer retries there is a chance that the messages order might change, because producer send messages in parallel. We need to consider this if we are using key based ordering and forcing all our messages with a key are going to one partition only.

	max.in.flight.requests.per.connection

*Default value : 5*
Set this value to 1 to ensure the order of messages but this will impact the performance of the producer. 

But this order is handled by idempotent producers (explained below) from Kafka 0.11 onward 

### c. Idempotent Producer (Stable & Safe Pipeline)

Kafka producer can introduce a duplicate message if he doesn't get the ack because of network errors. This message will be retried by producer which will create a duplicate request on broker. Solution for that is idempotent producer, each message is passed with an ID and broker detect that this ID is processed earlier so it just send ack to inform the producer.

To enable idempotent producer, set following property

	enable.idempotence = true

In background this setting set following low level settings

	retries = Max Integer Value
	max.in.flight.requests = 5
	acks = all

## 2. High Throughput Producer Configuration for Kafka

### a. Message Compression

	compression.type

*Possible values :* 
**'none'** : Default. No compression
**'gzip'** : High compression but slow
**'lz4'** : Average compression but fast
**'snappy'** : Average compression but fast

Bigger the batch, better the compression

### b. Producer Batching

Kafka use smart batching to send data in batches and consumer functionality need not to change for decompression.

The number of milliseconds a producer is willing to wait before sending a batch 

	linger.ms

*Default value : 0*

This can be increased a bit say 5 milliseconds to create a bigger batch size, hence better compression, with affecting delivery time a bit. 

The size of batch in KB after which kafka will create an batch and send the data.

	batch.size

*Default : 16 KB*

Can be set to 32 KB or 64 KB, do not set it to super high member.

==================================================
## Advance Configuration Kafka Consumer 

### a. Delivery Semantics for Consumer

* **At most once** : 
	Consumer / Consumer Group will commit the offsets as soon as it receive the message and then process the data. Possible data loss as consumer might go down during processing of the data.
	
* **At least once** : Recommended for kafka to outside communication 
	Consumer / Consumer Group will process the data (batch of data) and then commit the offsets. Possibility of duplicate processing of messages, as consumer can go down after successfully processing some of the messages from the batch. We should created **idempotent consumer** to handle this case.

* **Only once** :
	Only applicable for Kafka to Kafka communication. But this will ensure message is delivered & processed only once.

### b. Idempotent Consumer
 
 Ensuring the data/message is processed only once. Possible solution for this is to assign an ID to the message and then ensure consumer check this ID before processing.

*  Kafka Generic ID : 
	Record Topic + Record Partition + Record Offset
* Data Stream ID : 
	Let's say tweet id, facebook user id, google place id, database record id etc

Use data stream id of available else use kafka generic id

### c. Consumer offset commit strategies

Auto Commit & synchronous processing of batches : **Default**

	enable.auto.commit = true
	auto.commit.interval.ms = 5000

Manual Commit & synchronous processing of batches

	enable.auto.commit = false

With manual commit, we can collect the records array in a temp collection object and then process all records (such as storing into DB with batch processing or indexing into elastic search as batch request). Once complete collection is processed, manually call **consumer.commitSync()**

## Other Important points to consider

* Always create the topics with required partitions and replication factor manually OR change the default configuration for new topic creation
* Consider adding a run-time shutdown hook to close the consumer & producer cleanly.
* Set proper data retention period, 7 days is default as of now.
* To replay the offset, reset the offsets using kafka-consumer-group command


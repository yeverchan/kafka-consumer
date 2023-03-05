package com.example.minikafkaconsumer.service;

import com.example.minikafkaconsumer.config.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaConsumerRunner implements Runnable {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerRunner.class);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final KafkaProperties properties;

    private KafkaConsumer<String, String> kafkaConsumer;

    public KafkaConsumerRunner(KafkaProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer = new KafkaConsumer<>(properties.getConsumerProperties());

        try {
            kafkaConsumer.subscribe(Collections.singleton(properties.getTOPIC()));

            while (!closed.get()) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                log.info("Thread : {} polling....", Thread.currentThread().getName());

                records.forEach(this::handleRecord);

                kafkaConsumer.commitSync();
            }
        } catch (WakeupException we) {
            if (!closed.get()) {
                throw we;
            }
        } catch (Exception e) {
            log.error("Unexpected error {}", e.getMessage());
        } finally {
            kafkaConsumer.close();
        }
    }

    public void handleRecord(ConsumerRecord<String, String> record) {
        log.info("Current thread : {}, timestamp: {} topic : {}, value : {}, partition : {}, offset : {}",
                Thread.currentThread().getName(),
                record.timestamp(),
                record.topic(),
                record.value(),
                record.partition(),
                record.offset());
    }

    public void shutdown() {
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}

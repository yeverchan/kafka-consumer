package com.example.minikafkaconsumer.adapter;

import com.example.minikafkaconsumer.config.KafkaProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaMultiConsumer {

    private final Logger log = LoggerFactory.getLogger(KafkaMultiConsumer.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private static final String TOPIC = "test_topic";
    private static final int PARTITION_COUNT = 4;

    private final KafkaProperties kafkaProperties;

    public KafkaMultiConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void run() {

        log.info("Thread: {}, Consumer runners initializing...", Thread.currentThread().getName());

        for (int i = 0; i < PARTITION_COUNT; i++) {
            KafkaConsumerRunner runner = new KafkaConsumerRunner(kafkaProperties, TOPIC);

            executorService.execute(runner);
        }

        log.info("Consumer runners initialized");
    }
}

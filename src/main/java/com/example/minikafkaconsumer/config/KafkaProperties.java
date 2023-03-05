package com.example.minikafkaconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private static final List<String> BOOTSTRAP_SEVERS = List.of("127.0.0.1:9092", "127.0.0.1:9093", "127.0.0.1:9094");
    private static final String GROUP_ID = "test_consumer_group";

    private String TOPIC;

    private final Map<String, Object> consumerProperties;

    private final Map<String, Object> producerProperties;

    public KafkaProperties() {
        // config server 사용 시 공통 설정은 yml 설정으로 변경
        this.consumerProperties = new HashMap<>() {
            {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SEVERS);
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
                put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
            }
        };
        this.producerProperties = new HashMap<>();
    }

    public Map<String, Object> getConsumerProperties() {
        return consumerProperties;
    }

    public Map<String, Object> getProducerProperties() {
        return producerProperties;
    }

    public String getTOPIC() {
        return TOPIC;
    }

    public void setTOPIC(String TOPIC) {
        this.TOPIC = TOPIC;
    }
}

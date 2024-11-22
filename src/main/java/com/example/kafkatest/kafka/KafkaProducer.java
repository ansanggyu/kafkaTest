package com.example.kafkatest.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KafkaProducer {
    private static final String TOPIC = "mariadb-topic"; // Kafka 토픽 이름

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object message) {
        log.error("Sending Message to producer", message);
        kafkaTemplate.send(TOPIC, message);
    }
}

package com.example.kafkatest.kafka;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumer {
    @KafkaListener(topics = "mariadb-server-topic", groupId = "group_id")
    public void consumeMessage(String message) {
        System.out.println("Consumed message: " + message);
        // MongoDB로 데이터 전달 로직 추가
    }
}

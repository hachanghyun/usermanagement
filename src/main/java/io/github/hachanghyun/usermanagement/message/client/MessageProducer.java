package io.github.hachanghyun.usermanagement.message.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "message-topic";

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String phone, String message, String name) {
        String payload = String.join(",", phone, message, name);
        System.out.println("✅ Kafka 전송 시도: " + payload);
        CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(TOPIC, payload).completable();

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata meta = result.getRecordMetadata();
                log.info("✅ Kafka 전송 성공: payload={}, offset={}, partition={}",
                        payload, meta.offset(), meta.partition());
            } else {
                log.error("❌ Kafka 전송 실패: payload={}, error={}", payload, ex.getMessage());
            }
        });
    }
}

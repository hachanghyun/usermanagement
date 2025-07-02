package io.github.hachanghyun.usermanagement.message.client;

import io.github.hachanghyun.usermanagement.message.payload.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class MessageProducer {

    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private static final String TOPIC = "message-topic";

    public MessageProducer(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String phone, String message, String name) {
        MessagePayload payload = new MessagePayload(phone, message, name);

        log.info("ㄴKafka 전송: {}", payload);
        CompletableFuture<SendResult<String, MessagePayload>> future = kafkaTemplate.send(TOPIC, payload);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata meta = result.getRecordMetadata();
                log.info("Kafka 전송 성공: payload={}, offset={}, partition={}", payload, meta.offset(), meta.partition());
            } else {
                log.error("Kafka 전송 실패: payload={}, error={}", payload, ex.getMessage());
            }
        });
    }
}

package io.github.hachanghyun.usermanagement.message.client;

import io.github.hachanghyun.usermanagement.message.dto.KakaoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageConsumer {

    private final WebClient kakaoClient;
    private final WebClient smsClient;

    @KafkaListener(topics = "message-topic", groupId = "message-group")
    public void consume(String payload) {
        try {
            String[] parts = StringUtils.splitPreserveAllTokens(payload, ",", 3); // phone,message,name
            if (parts.length < 3) {
                log.warn("잘못된 payload: {}", payload);
                return;
            }

            String phone = parts[0];
            String message = parts[1];
            String name = parts[2];
            String fullMessage = name + "님, 안녕하세요. 현대 오토에버입니다.\n" + message;

            kakaoClient.post()
                    .uri("/kakaotalk-messages")
                    .bodyValue(new KakaoRequest(phone, fullMessage))
                    .retrieve()
                    .onStatus(s -> s.value() >= 400, clientResponse -> {
                        log.warn("카카오톡 실패, SMS로 전환: phone={}", phone);
                        return smsClient.post()
                                .uri(uriBuilder -> uriBuilder.path("/sms").queryParam("phone", phone).build())
                                .bodyValue("message=" + fullMessage)
                                .retrieve()
                                .toBodilessEntity()
                                .doOnSuccess(r -> log.info("SMS 전송 성공: phone={}", phone))
                                .doOnError(e -> log.error("SMS 전송 실패", e))
                                .then(Mono.empty());
                    })
                    .toBodilessEntity()
                    .doOnSuccess(r -> log.info("카카오톡 전송 성공: phone={}", phone))
                    .doOnError(e -> log.error("카카오톡 전송 실패", e))
                    .subscribe();

        } catch (Exception e) {
            log.error("메시지 처리 중 예외 발생", e);
        }
    }
}

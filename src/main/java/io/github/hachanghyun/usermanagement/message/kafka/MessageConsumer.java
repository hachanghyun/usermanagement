package io.github.hachanghyun.usermanagement.message.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class MessageConsumer {

    private final WebClient kakaoClient;
    private final WebClient smsClient;
    private final StringRedisTemplate redisTemplate;

    private static final String KAKAO_LIMIT_KEY = "kakao:limit";
    private static final String SMS_LIMIT_KEY = "sms:limit";
    private static final int KAKAO_LIMIT = 100;
    private static final int SMS_LIMIT = 500;

    @KafkaListener(topics = "message-topic", groupId = "message-group")
    public void consume(String payload) {
        String[] parts = payload.split(",", 3); // phone,message,name
        String phone = parts[0];
        String message = parts[1];
        String name = parts[2];
        String fullMessage = name + "님, 안녕하세요. 현대 오토에버입니다.\n" + message;

        if (increment(KAKAO_LIMIT_KEY) <= KAKAO_LIMIT) {
            sendKakao(phone, fullMessage);
        } else if (increment(SMS_LIMIT_KEY) <= SMS_LIMIT) {
            sendSms(phone, fullMessage);
        } else {
            log.warn("모든 채널 제한 초과: {}", phone);
        }
    }

    private void sendKakao(String phone, String fullMessage) {
        kakaoClient.post()
                .uri("/kakaotalk-messages")
                .bodyValue(new KakaoRequest(phone, fullMessage))
                .retrieve()
                .onStatus(s -> s.value() >= 400, clientResponse -> {
                    log.warn("카카오톡 실패, SMS로 전환");
                    return sendSmsFallback(phone, fullMessage);
                })
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("카카오톡 전송 성공"))
                .doOnError(e -> log.error("카카오톡 전송 실패", e))
                .subscribe();
    }

    private Mono<Void> sendSmsFallback(String phone, String fullMessage) {
        if (increment(SMS_LIMIT_KEY) <= SMS_LIMIT) {
            return smsClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/sms").queryParam("phone", phone).build())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("message=" + fullMessage)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(r -> log.info("SMS 전송 성공"))
                    .doOnError(e -> log.error("SMS 전송 실패", e))
                    .then();
        } else {
            log.warn("SMS 전송 제한 초과");
            return Mono.empty();
        }
    }

    private int increment(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        return count != null ? count.intValue() : 0;
    }
}

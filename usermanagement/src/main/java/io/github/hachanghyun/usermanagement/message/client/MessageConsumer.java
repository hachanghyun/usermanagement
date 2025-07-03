package io.github.hachanghyun.usermanagement.message.client;

import io.github.hachanghyun.usermanagement.message.dto.KakaoRequest;
import io.github.hachanghyun.usermanagement.message.payload.MessagePayload;
import io.github.hachanghyun.usermanagement.message.service.KakaoRateLimiterService;
import io.github.hachanghyun.usermanagement.message.service.SmsRateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageConsumer {

    private final WebClient kakaoClient;
    private final WebClient smsClient;
    private final KakaoRateLimiterService kakaoRateLimiterService;
    private final SmsRateLimiterService smsRateLimiterService;

    @KafkaListener(topics = "message-topic", groupId = "message-group")
    public void consume(MessagePayload payload) {
        log.info("Kafka 수신: {}", payload);

        try {
            String phone = payload.getPhoneNumber();
            String message = payload.getMessage();
            String name = payload.getName();
            //!TODO 스트링 수정
            String fullMessage = name + "님, 안녕하세요. ㅇㅇㅇㅇㅇㅇ입니다.\n" + message;

            if (kakaoRateLimiterService.tryAcquire("kakao-send")) {
                kakaoClient.post()
                        .uri("/kakaotalk-messages")
                        .bodyValue(new KakaoRequest(phone, fullMessage))
                        .retrieve()
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> {
                                    log.warn("카카오톡 API 응답 오류: status={}", response.statusCode());
                                    return Mono.error(new RuntimeException("카카오톡 전송 실패"));
                                }
                        )
                        .toBodilessEntity()
                        .doOnSuccess(r -> log.info("카카오톡 전송 성공: phone={}", phone))
                        .onErrorResume(ex -> {
                            log.warn("카카오톡 실패, SMS로 대체 전송: {}", phone);
                            return sendSmsFallback(phone, fullMessage)
                                    .then(Mono.empty()); // Mono<ResponseEntity<Void>> 타입에 맞춤
                        })
                        .subscribe();
            } else {
                log.warn("카카오톡 분당 전송 제한 초과 - {}는 SMS로 전환", phone);
                sendSmsFallback(phone, fullMessage).subscribe();
            }

        } catch (Exception e) {
            log.error("메시지 처리 중 예외 발생", e);
        }
    }

    private Mono<Void> sendSmsFallback(String phone, String fullMessage) {
        if (!smsRateLimiterService.tryAcquire("sms-send")) {
            log.warn("SMS 분당 전송 제한 초과 - {}는 전송 제외", phone);
            return Mono.empty();
        }

        return smsClient.post()
                .uri(uriBuilder -> uriBuilder.path("/sms").queryParam("phone", phone).build())
                .body(BodyInserters.fromFormData("message", fullMessage))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("SMS 전송 성공: phone={}", phone))
                .doOnError(e -> log.error("SMS 전송 실패", e))
                .then();
    }
}

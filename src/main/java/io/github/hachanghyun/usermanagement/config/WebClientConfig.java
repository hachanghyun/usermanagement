package io.github.hachanghyun.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "1234");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

    @Bean
    public WebClient smsClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("autoever", "5678");
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }
}

package io.github.hachanghyun.usermanagement.message.service;

import io.github.hachanghyun.usermanagement.message.client.MessageProducer;
import io.github.hachanghyun.usermanagement.user.entity.User;
import io.github.hachanghyun.usermanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final MessageProducer messageProducer;
    private final RedisTemplate<String, String> redisTemplate;

    public void sendMessagesByAgeGroup(String ageGroupText, String message) {
        int age;
        try {
            log.info("@@@@@@@@@TEST222@@@@@@@@");
            age = Integer.parseInt(ageGroupText.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지 않은 연령대 형식입니다. 예: 20대, 30대");
        }

        List<User> users = userRepository.findAll();
        log.info("📋 전체 유저 수: {}", users.size());
        int currentYear = LocalDate.now().getYear();

        users.stream()
                .filter(user -> {
                    String rrn = user.getResidentRegistrationNumber();
                    if (rrn == null || rrn.length() < 2) return false;
                    int birthYearPrefix = rrn.charAt(6) < '3' ? 1900 : 2000;
                    int birthYear = birthYearPrefix + Integer.parseInt(rrn.substring(0, 2));
                    int userAge = currentYear - birthYear;
                    return (userAge / 10) * 10 == age;
                })
                .forEach(user -> {
                    String key = "msg:lock:" + user.getPhoneNumber();
                    Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, "1");

                    if (Boolean.TRUE.equals(acquired)) {
                        log.info("📲 Kafka 메시지 전송 대상: {}", user.getPhoneNumber());
                        redisTemplate.expire(key, java.time.Duration.ofMinutes(1));
                        messageProducer.sendMessage(user.getPhoneNumber(), message, user.getName());
                    }
                });
    }

}

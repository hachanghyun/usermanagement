package io.github.hachanghyun.usermanagement.test;

import io.github.hachanghyun.usermanagement.user.entity.User;
import io.github.hachanghyun.usermanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("generate-users") // 실행할 때만 이 프로필 활성화
public class DummyUserGenerator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("이미 유저가 존재함. 생성 생략.");
            return;
        }

        IntStream.rangeClosed(1, 700).forEach(i -> {
            int age = 10 + random.nextInt(70); // 10~79세 랜덤
            String phone = String.format("010%08d", i);
            User user = User.builder()
                    .account("user" + i)
                    .password("pass" + i)
                    .name("테스트" + i)
                    .residentRegistrationNumber(generateRRN(i))
                    .phoneNumber(phone)
                    .address("서울시 어딘가 " + i)
                    .build();
            userRepository.save(user);
        });

        System.out.println("✅ 700명 더미 유저 생성 완료");
    }

    private String generateRRN(int i) {
        int group = i % 5;
        String birth;
        String gender;

        switch (group) {
            case 0: // 10대
                birth = "101010";
                gender = "3";
                break;
            case 1: // 20대
                birth = "000101";
                gender = "3";
                break;
            case 2: // 30대
                birth = "900101";
                gender = "1";
                break;
            case 3: // 40대
                birth = "800101";
                gender = "1";
                break;
            default: // 50대
                birth = "700101";
                gender = "1";
                break;
        }

        return birth + "-" + gender + String.format("%06d", i);
    }

}

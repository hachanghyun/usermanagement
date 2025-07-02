# usermanagement

## 도커 실행
    ./gradlew build         # 먼저 Spring Boot jar 빌드
    docker-compose down -v # 이전 포트 설정 삭제 + 볼륨 초기화
    docker-compose up --build # 새 포트 설정 반영해서 컨테이너 재생성

    ./gradlew build
    docker-compose down -v
    docker-compose up --build

    docker-compose up -d mariadb
    ./gradlew clean build
    

## 로그 확인
    docker logs -f spring-boot-app

## 회원가입 API 테스트
    curl -X POST http://localhost:8080/users/signup \
    -H "Content-Type: application/json" \
    -d '{
    "account": "test123",
    "password": "1234",
    "name": "하창현",
    "residentRegistrationNumber": "9001011234567",
    "phoneNumber": "01012345678",
    "address": "서울특별시 금천구"
    }'

## 관리자 페이지 접속
    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 30, "message": "공지입니다."}'

## 카프카 토픽 전체 조회
    docker exec -it <kafka-container-name> kafka-topics.sh --bootstrap-server localhost:9092 --list

    docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --list


## 특정 토픽 내용 조회 
    docker exec -it <kafka-container-name> kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic message-topic \
    --from-beginning

    docker exec -it kafka kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic message-topic \
    --from-beginning

## 더미 회원가입 데이터 생성 (700명)
    SPRING_PROFILES_ACTIVE=generate-users ./gradlew bootRun

## pdf에 깃허브주소 추가!!!!


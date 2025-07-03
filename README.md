# usermanagement

## Redis는 "중복 메시지 전송을 방지하기 위한 분산 락(lock)" 용도로 사용

## 도커 실행
    # 먼저 Spring Boot jar 빌드
    ./gradlew build    

    # 이전 포트 설정 삭제 + 볼륨 초기화
    docker-compose down -v 

    # 새 포트 설정 반영해서 컨테이너 재생성
    docker-compose up --build 
    
    # DB 포함 실행
    docker-compose -f docker-compose.yml -f docker-compose.db.yml up --build

    # DB 없이 실행
    docker-compose -f docker-compose.yml up --build 

    # 쉘파일 실행
    chmod +x run-without-db.sh
    ./run-without-db.sh

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

## 로그인 및 내 정보 조회

### 로그인 → JWT 토큰 획득
    curl -X POST http://localhost:8080/users/login \
    -H "Content-Type: application/json" \
    -d '{
    "account": "test123",
    "password": "1234"
    }'

### 위 명령 결과에서 "message" 필드에 있는 토큰 값을 추출한 후, 아래 명령에서 ${JWT_TOKEN} 자리에 넣어주세요.

# 내 정보 조회
    curl -X GET http://localhost:8080/users/me \
    -H "Authorization: Bearer ${JWT_TOKEN}"

    curl -X GET http://localhost:8080/users/me \
    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MTIzIiwiaWF0IjoxNzUxNDk5ODcxLCJleHAiOjE3NTE1MDM0NzF9.LeWlKw2IEfh7SJ5YCYU4TeZV9bXpSJuEOiqlfAl0RyQ"

## 전체 사용자 조회
    curl -X GET http://localhost:8080/admin/users \
    -H "Authorization: Basic YWRtaW46MTIxMg=="

## 사용자 정보 수정 (예: userId=1 가정)
    curl -X PUT http://localhost:8080/admin/users/1 \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{
    "address": "경기도 수원시"
    }'

## 사용자 삭제 (예: userId=1 가정)
    curl -X DELETE http://localhost:8080/admin/users/1 \
    -H "Authorization: Basic YWRtaW46MTIxMg=="

## kafka 메시지 전송 (예: 30대 대상 공지)
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

## 도커 스프링부트 로그 확인
    docker-compose logs -f spring-boot-app

## 도커 컨테이너 확인
    docker ps
## name 으로 확인

## MariaDB 컨테이너에 접속
    docker exec -it my-mariadb bash

## mysql 클라이언트 실행 (컨테이너 안에서)
    mysql -u root -p
    password: rootpass

##  원하는 데이터베이스 선택 후 쿼리 실행
    show databases;
    USE userdb;
    SELECT * FROM users;

## redis 키 조회 
    # 모든 키 확인
    curl http://localhost:8080/dev/redis/keys

    # 특정 prefix로 필터링 (예: msg:lock:)
    curl "http://localhost:8080/dev/redis/keys?pattern=msg:lock:*"

## pdf에 깃허브주소 추가!!!!

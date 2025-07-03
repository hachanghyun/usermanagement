# usermanagement

## 도커 파일 실행
    # 쉘파일 실행
    chmod +x docker-init.sh
    ./docker-init.sh

## 로그 확인
    # 도커 컨테이너 실행 확인
    docker ps
    
    # 컨테이너 로그 확인
    docker logs -f 컨테이너명

## curl 명령어로 API 테스트
    # 1.회원가입 API 테스트
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

    # 2.시스템 관리자 API 테스트
    ## 전체 사용자 조회
    curl -X GET http://localhost:8080/admin/users -u admin:1212

    ## 사용자 정보 수정 (예: userId=1 가정)
    curl -X PUT http://localhost:8080/admin/users/3000 \
    -u admin:1212 \
    -H "Content-Type: application/json" \
    -d '{"address": "경기도 수원시"}'

    ## 사용자 삭제 (예: userId=1 가정)
    curl -X DELETE http://localhost:8080/admin/users/3000 -u admin:1212

    # 3.로그인 → JWT 토큰 획득
    curl -X POST http://localhost:8080/users/login \
    -H "Content-Type: application/json" \
    -d '{
    "account": "test123",
    "password": "1234"
    }'
    # 위 명령 결과에서 "message" 필드에 있는 토큰 값을 추출한 후, 아래 명령에서 jwt 자리에 넣어주세요.

    # 4.로그인 한 사용자의 자신의 회원 상세 정보 조회 테스트
    curl -X GET http://localhost:8080/users/me \
    -H "Authorization: Bearer jwt"

    # 5.관리자API) 연령대별 kafka 메시지 전송 테스트 
    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 20, "message": "blabla"}'

    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 30, "message": "blabla"}'

## 카프카 관련 명령어
    # 카프카 토픽 전체 조회
    docker exec -it <kafka-container-name> kafka-topics.sh --bootstrap-server localhost:9092 --list

    # 특정 토픽 내용 조회
    docker exec -it <kafka-container-name> kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic message-topic \
    --from-beginning

## MariaDB 관련 명령어
    # MariaDB 컨테이너에 접속
    docker exec -it mariadb bash

    # mysql 클라이언트 실행 (컨테이너 안에서)
    mysql -u root -p
    password: rootpass

    # 원하는 데이터베이스 선택 후 쿼리 실행
    show <databases>;
    use <db>;
    SELECT * FROM users;

## Redis 관련 명령어
    # 모든 키 확인
    curl http://localhost:8080/dev/redis/keys

    # 특정 prefix로 필터링 (예: msg:lock:)
    curl "http://localhost:8080/dev/redis/keys?pattern=msg:lock:*"

## Redis 키 조회2
    # 모든 키 확인
    docker exec -it full-project-redis-1 redis-cli
    127.0.0.1:6379> keys *

20 토픽안되는거 확인 
# 5.관리자API) 연령대별 kafka 메시지 전송 테스트
    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 20, "message": "blabla"}'
basic 보내는거 변경하기
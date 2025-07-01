# usermanagement

## 도커 실행
    docker-compose down -v
    docker-compose up --build

## 관리자 페이지 접속
    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 30, "message": "30대 유저에게 공지합니다."}'

    curl -X POST http://localhost:8080/admin/messages \
    -H "Authorization: Basic YWRtaW46MTIxMg==" \
    -H "Content-Type: application/json" \
    -d '{"ageGroup": 30, "message": "공지입니다."}'

## 도커 실행
    docker-compose down -v
    docker-compose up --build

## 로그 확인
    docker logs -f spring-boot-app

## 회원가입 API 테스트
    curl -X POST http://localhost:8080/users/signup \
    -H "Content-Type: application/json" \
    -d '{
    "account": "test123",
    "password": "1234",
    "name": "홍길동",
    "residentRegistrationNumber": "9001011234567",
    "phoneNumber": "01012345678",
    "address": "서울특별시 강남구"
    }'

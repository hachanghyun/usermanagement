# PROMPT 기반 개발 기록
    이 프로젝트는 GPT 기반 LLM과 GitHub Copilot을 활용한 **프롬프트 엔지니어링 기반 개발 사례**임.  
    단순 자동완성을 넘어서 **설계부터 디버깅까지 전 과정에서 AI와 협업**하며 시스템을 구축함.

## 사용 도구

| 도구              | 사용 목적                                  |
|-------------------|----------------------------------------|
| ChatGPT (GPT-4o)  | 시스템 아키텍처 설계, Kafka/Redis 설계, 인증 처리, 예외 처리 등 |
| GitHub Copilot    | 반복적인 코드 작성, JPA 쿼리 생성, 템플릿 코드 자동 완성 등| 

## 기술 스택
    - Java 17
    - Spring Boot 3.5.3
    - Spring Data JPA
    - Gradle
    - H2 Database
    - Lombok
    - Swagger UI (OpenAPI 3)
    - Kafka
    - Redis
    - JWT 인증
    - Docker / Docker Compose 

## 회원가입 기능

### 목적
    사용자 정보 등록 API 설계

### 프롬프트
    Spring Boot로 회원가입 API 만들어줘.  
    필드: 계정, 비밀번호, 이름, 주민등록번호, 전화번호, 주소  
    주민등록번호, 계정 중복 불가  
    비밀번호는 평문 저장  
    인증은 필요 없음  
    JPA와 Lombok 사용

## 관리자 API

### 목적
    관리자 전용 회원 조회/수정/삭제 기능 구성

### 프롬프트
    관리자만 접근할 수 있는 API 만들어줘  
    Basic Auth 인증 걸고  
    회원 목록 조회는 페이지네이션  
    수정은 비밀번호나 주소 중 하나만 또는 둘 다 수정 가능해야 함  
    삭제 API도 포함  
    JPA와 DTO 구조로 설계

## Kafka 메시지 전송 (연령대별 토픽 분리)

### 목적
    사용자 연령대에 따라 서로 다른 Kafka 토픽으로 메시지 전송

### 프롬프트
    Spring Boot에서 Kafka Producer 만들어줘  
    연령대에 따라 토픽을 다르게 전송하고 싶어  
    예: 20대는 message-topic-20s, 30대는 message-topic-30s  
    JSON 직렬화 사용  
    연령대 구분은 주민등록번호로 처리

## Kafka Consumer 분리

### 목적
    Kafka Consumer를 연령대별로 따로 분리해서 처리

### 프롬프트
    Kafka Consumer 클래스를 연령대별로 나누고  
    각 토픽을 각각 구독하도록 구성해줘  
    예: Consumer20s는 message-topic-20s, Consumer30s는 message-topic-30s  
    Listener 설정은 각 클래스에 따로 적용

## Redis 중복 전송 방지

### 목적
    같은 사용자에게 중복 메시지 전송되지 않도록 제한

### 프롬프트
    전화번호를 기반으로 Redis에 Lock 걸어줘  
    msg:lock:{phone} 키로 저장하고  
    1분 정도 유지되는 분산락 방식으로 구성

---

## Redis 전송 속도 제한

### 목적
    API 호출 횟수를 분당 기준으로 제한

### 프롬프트
    Redis로 분당 전송 제한 걸고 싶어  
    카카오톡은 분당 100건, SMS는 분당 500건  
    각 서비스별로 키 다르게 설정해줘  
    limit:{service}:{key} 형태로 구성

## WebClient 메시지 전송

### 목적
    WebClient로 메시지 전송 요청 수행

### 프롬프트
    Spring WebClient로 외부 API에 POST 요청 보내줘  
    헤더에 Basic Auth 포함하고  
    application/json 형식으로 요청 바디 전송  
    인증 실패나 응답 오류 처리도 같이 구성

## JWT 인증 처리

### 목적
    JWT 토큰을 통한 사용자 인증 구성

### 프롬프트
    JWT 토큰 생성하고 필터로 인증 처리해줘  
    Authorization 헤더에서 토큰 꺼내서 유효성 검증  
    토큰에 사용자 account 정보 포함  
    인증 성공 시 SecurityContext에 사용자 등록  
    필터는 OncePerRequestFilter로 구성

## 주민등록번호 → 출생년도 계산

### 목적
    주민등록번호 앞자리와 뒷자리 성별코드로 출생년도 계산

### 프롬프트
    주민등록번호 앞 6자리로 출생년도 계산해줘  
    뒷자리 첫 숫자가 1~2면 1900년대, 3~4면 2000년대로 간주  
    예: 990101-1234567 → 1999년  
    010101-3456789 → 2001년

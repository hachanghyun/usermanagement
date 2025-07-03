

# Redis 분산락 구조 요약
## 단계	동작 내용
    setIfAbsent 시도	"msg:lock:{phone}" key가 Redis에 있는지 확인
    없다면 → 메시지 Kafka로 전송 & expire 1분 설정
    있다면 → 이미 전송된 사용자이므로 전송 생략

## 왜 Redis를 써야 할까?
    Spring Boot 인스턴스가 여러 개 떠 있을 수 있음 (e.g. 도커 컨테이너 여러 개)
    이 경우 동시에 같은 사용자에게 중복 메시지를 보낼 수 있음
    Redis는 중앙 저장소라서 → 모든 인스턴스에서 락 공유 가능
    따라서 동시성 제어와 중복 방지 역할을 완벽하게 해줌

## 예시 흐름 (전화번호 01012345678)
    Kafka 전송 전 msg:lock:01012345678 존재 여부 확인
    없다면:
        Redis에 저장 (setIfAbsent)
        TTL 1분 설정 (expire)
    Kafka 전송
    있다면:
        이미 보냈으므로 무시 (중복 전송 방지)
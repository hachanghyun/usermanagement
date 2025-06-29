
# PROMPT.md

## 세팅
    chatGPT GPT-4o 모델 사용

## 회원가입 API 구현
    '''
    목적:
    Spring Boot로 회원가입 기능 구현
    입력값: 계정, 비밀번호, 이름, 주민등록번호, 휴대폰번호, 주소
    
    조건:
    - 계정, 주민등록번호 중복 X
    - 비밀번호 평문 저장
    - 요청은 그대로 신뢰
    - 인증/로그인 기능 없음
    
    환경:
    - Java 17
    - Spring Boot 3.5.3
    - Spring Data JPA
    - application.yaml 형식
    - gradle 빌드
    - H2 Database
    - Lombok
    - Swagger UI
    '''

## 시스템 관리자용 API 구현
    '''
    목적:  
    시스템 관리자가 회원 정보를 조회, 수정, 삭제할 수 있는 API 구현
    
    기능:
    - 회원 조회 (pagination 기반)
    - 회원 수정 (비밀번호, 주소 수정 가능 – 하나만 수정 또는 둘 다 수정 가능)
    - 회원 삭제
    
    조건:
    - 수정 시 비밀번호 또는 주소 중 하나만 수정도 가능하며, 둘 다 수정도 가능해야 함
    - 회원 조회는 pagination 기반으로 응답
    - 인증 수단 : Basic Auth, 사용자명: admin, 암호: 1212
    
    환경:
    - 이전환경과 동일
    '''

# PreemptiveBookcafe
공주대학교 북카페에 좌석신고 제도를 도입하여 좌석 사석화를 방지하고 북카페 사용률과 순환율을 증진시키기 위한 프로젝트입니다.

## 프로젝트 기술 스택
 - Java8
 - MySQL
 - JPA/Hibernate/Spring Data JPA
 - SpringBoot 2.6
 - Gradle
 - AWS EC2
 - Firebase FCM (Firebase Cloud Messaging)

## ISSUE
 1. 스프링과 같은 멀티스레드 환경에서 타이머를 어떻게 구현할까?
  - 비동기 처리
   타이머 시작 시 비동기 스레드를 만들어 해당 시간만큼 sleep 이후 특정 로직을 수행하는 메소드를 호출할 수 있도록 처리
 
 2. 비동기 처리 한 스레드에 어떻게 접근해서 다음 로직을 수행하지 못하게 할 수 있을까?
  - ThreadPoolExecutor
   ThreadPoolExecutor을 이용해 비동기 스레드 생성 시 이름을 붙일 수 있도록 하고 스레드 풀을 관리
   로직 : 생성된 스레드 이름을 DB에 저장 -> 현재 돌고 있는 모든 스레드 중 동일 이름 스레드 조회 -> 해당 스레드 interrupt -> 해당 비동기 스레드 종료

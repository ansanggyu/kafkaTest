# Kafka 및 Debezium 설정과 토픽 생성 시나리오

## Kafka 및 Debezium 설정

1. Kafka에서 토픽을 생성하는 명령어:  
   kafka-topics --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

2. Kafka에 토픽 리스트 확인:  
   kafka-topics --list --bootstrap-server localhost:9092

3. Debezium에서 MariaDB의 변경사항을 확인하는 부분.

4. my.cnf 경로를 찾기 위한 명령어:  
   find / -name "my.cnf"

5. vi, vim, nano 편집기 설치 후 nano /etc/mysql/my.cnf로 열고 아래 코드 입력:
[mysqld]
log-bin = mysql-bin
server-id = 1
binlog-format = row
binlog_row_image=FULL


---

## Kafka 토픽 생성 시나리오

### 관리자 시스템
- 점주: 이벤트 지점 등록 시 알림.
- 제작자: 상품 등록 승인.  
*(보류) 제작자 신청 시 등급 상승 메시지 (단, 소셜 로그인일 경우).*

### 점주 시스템
- 관리자: 이벤트 관련 메시지, 상품 관련 판매 내역.
- 제작자: 발주 처리.
- 소비자: 온라인 주문 시 QR 코드를 받을 수 있는 URL 또는 교환 값(string)을 전달.

### 제작자 시스템
- 관리자: 제작자 등록 시, 상품 등록 시, 이벤트 신청 시.
- 점주: 발주 물품이 성공적으로 처리되었을 경우.

### 소비자 시스템
- 점주: 상품 교환권 구매 시.

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

#### 토픽 정의
- **토픽명**: Consumer가 구독할 토픽 이름.
- **구조**: 각 토픽의 데이터 형식(JSON 또는 스트림 데이터).

| 시스템       | 시나리오                                   | 토픽명                              | 메시지 데이터 형식                          |
|--------------|--------------------------------------------|--------------------------------------|--------------------------------------------|
| 관리자 시스템 | 이벤트 지점 등록 시 점주에게 알림          | `admin.to.store.event-notification` | JSON `{storeId, eventId, eventTitle, ...}` |
| 관리자 시스템 | 제작자 상품 등록 승인                     | `admin.to.creator.product-approval` | JSON `{creatorId, productId, approvalStatus, ...}` |
| 점주 시스템   | 소비자에게 QR 코드 또는 교환 값 전달        | `store.to.consumer.qr-code`         | JSON `{consumerId, orderId, qrCodeUrl, ...}` |
| 제작자 시스템 | 점주에게 발주 물품 처리 완료 메시지         | `creator.to.store.order-completion` | JSON `{storeId, orderId, productId, ...}` |
| 소비자 시스템 | 점주에게 상품 교환권 구매 메시지           | `consumer.to.store.voucher-purchase`| JSON `{consumerId, storeId, voucherId, ...}` |

---

### 1.2 브로커 설계
Kafka 클러스터의 안정성과 성능을 고려한 브로커 설계.

#### 브로커 구성
- **브로커 개수**: 최소 3개.
- **복제 계수 (Replication Factor)**: 2~3 (브로커 장애 시 복구 가능).
- **파티션 개수 (Partitions)**: 각 토픽마다 분산 처리를 위해 적절히 설정 (예: 3~6).

---

### 1.3 주키퍼 vs Kraft
Kafka 클러스터의 메타데이터 관리 방식 선택.

| **옵션**  | **설명**                                                                                     |
|-----------|---------------------------------------------------------------------------------------------|
| **주키퍼** | 전통적인 방식으로 Kafka의 메타데이터와 브로커 관리를 담당. 현재도 안정적이며 많이 사용됨.         |
| **Kraft**  | Kafka 2.8 이상에서 제공하는 새로운 메타데이터 관리 방식. Zookeeper 없이 Kafka 자체로 관리 가능. |

> **추천**: 주키퍼는 안정적이고 현재 많은 시스템에서 사용되므로 우선 선택. 새로운 프로젝트에서는 Kraft를 고려.

---

### 1.4 메시지 데이터 형식
Kafka 메시지의 데이터 형식은 주로 JSON을 사용하며, 직렬화 도구로 Avro 또는 Protobuf를 고려할 수 있습니다.

- **JSON**:
  - 가독성이 좋고 사용이 쉬움.
  - 동적 데이터 처리에 적합.
- **Streams**:
  - Kafka Streams를 활용한 실시간 데이터 처리에 사용.

---

### 1.5 네트워크 설정
Consumer가 Kafka 브로커와 통신할 때 필요한 네트워크 설정.

#### 브로커 네트워크 구성
- **내부 네트워크**: 브로커 간 통신은 Docker Compose의 `bridge` 네트워크로 처리.
- **외부 네트워크**: Consumer가 외부에서 연결 시 Kafka 브로커의 `KAFKA_ADVERTISED_LISTENERS` 설정이 필요.


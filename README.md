카프카에서 토픽을 생성하는 명령어: 
kafka-topics --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

카프카에 토픽 리스트 확인:
kafka-topics --list --bootstrap-server localhost:9092

debezium에서 mariadb의 변경사항을 확인 하는 부분

bash     find / -name "my.cnf" << 명령어로 my.cnf경로를 찾기

vi나 vim nano 편집기 install하고 nano /etc/mysql/my.cnf로 열고

코드 입력하기.
[mysqld]
log-bin = mysql-bin
server-id = 1
binlog-format = row
binlog_row_image=FULL


시나리오.
관리자 	- 점주  (이벤트 지점 등록시 알림)
	  	- 제작자 (상품등록 승인
(보류: 제작자 신청시 등급오름 메세지)(단,소셜로그인일떄)

점주		-관리자 (이벤트관련 메세지, 상품관련판매내역)
		-제작자 (발주 처리)
		-소비자 (소비자 온라인 주문시 큐알을 받을 수 있는 url이나 교환값(string)을 소비자에게)

제작자	-관리자 (제작자 등록을 할 때, 상품 등록을 할때, 이벤트 신청할 때)
--------------------------------------------------------------------------------------------
		-점주
		-소비자

소비자	-점주
		-제작자
		-관리자

kafka zoo

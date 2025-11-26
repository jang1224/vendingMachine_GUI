# 자판기 프로그램 (vendingMachine_GUI) 실행 방법

이 프로그램은 Java Swing과 MySQL 데이터베이스를 사용합니다.
프로그램을 실행하기 전, 다음 단계를 꼭 수행해야 합니다.

## 1. MySQL 서버 설치
- 이 PC에 MySQL 서버가 설치되어 있어야 합니다. (버전 8.0 이상 권장)

## 2. 데이터베이스 및 테이블 생성
- MySQL Workbench 또는 CLI를 사용하여 `root` 계정으로 접속합니다.
- 함께 제출한 `vendingMachineDB.sql` 파일의 내용을 전체 복사하여,
- MySQL 쿼리 창에 붙여넣고 실행(Execute)합니다.
- (이 스크립트는 `vending_machine` 데이터베이스를 생성하고, `products` 테이블을 만든 뒤, 초기 재고 데이터를 삽입합니다.)

## 3. 본인의 DB정보로 수정하기
- model 패키지의 Inventory 클래스에서 IntelliJ에서 MySQL의 DB를 연결하실때 DB_URL, DB_USER, DB_PASSWORD를 수정하셔야 합니다.

## 4. 프로그램 실행
- IntelliJ에서 `src/view/MainEx.java` 파일을 열고 `main` 메서드를 실행합니다.
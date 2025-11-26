-- 각 쿼리를 Ctrl+Enter로 실행합니다.
CREATE DATABASE vending_machine;
USE vending_machine;
CREATE TABLE products (
    name      VARCHAR(100) PRIMARY KEY,
    price     INTEGER,
    quantity  INTEGER,
    imagePath VARCHAR(100)
);

INSERT INTO products(name, price, quantity, imagePath) VALUES
("코카콜라", 1200, 10, "/image/cocaCola.jpg"),
("칠성사이다", 1200, 10, "/image/cider.jpg"),
("레쓰비", 900, 10,"/image/letsBe.jpg"),
("밀키스", 1100, 10,"/image/milkis.jpg"),
("코카콜라 제로", 1200, 10,"/image/cocaColaZero.jpg"),
("데자와", 900, 10,"/image/tejava.jpg"),
("환타", 1100, 10,"/image/fanta.jpg"),
("물", 600, 10,"/image/samdasu.jpg"),
("하늘보리", 1300, 10,"/image/haneulBori.jpg"),
("핫식스", 800, 10,"/image/hox6.jpg"),
("초코송이", 1000, 10,"/image/chocoSongi.jpg"),
("칸쵸", 1100, 10,"/image/kancho.jpg"),
("버터와플", 1500, 10,"/image/butterWaffle.jpg"),
("오레오", 1200, 10,"/image/oreo.jpg"),
("빼빼로", 1000, 10,"/image/pepero.jpg"),
("쿠쿠다스", 2000, 10,"/image/kukudas.jpg"),
("가나초콜릿", 2000, 10,"/image/ghanaChocolate.jpg"),
("허쉬초콜릿", 2500, 10,"/image/hersheysChocolate.jpg"),
("쫄병", 1600, 10,"/image/jjolbyeong.jpg"),
("예감", 1400, 10,"/image/yegam.jpg"),
("빈츠", 1500, 10,"/image/binch.jpg");

SELECT * FROM products;
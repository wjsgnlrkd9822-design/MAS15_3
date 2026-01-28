-- Active: 1767840691076@@127.0.0.1@3306@aloha
SET FOREIGN_KEY_CHECKS = 0;
drop TABLE IF EXISTS `users`;
CREATE TABLE `users`( 
    `no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '회원번호',
    `id` VARCHAR(36) NOT NULL UNIQUE COMMENT '아이디',
    `username` VARCHAR(100) NOT NULL COMMENT '사용자아이디',
    `password` VARCHAR(100) NOT NULL COMMENT '비밀번호',
    `name` VARCHAR(100) NOT NULL COMMENT '이름',
    `birth` VARCHAR(10) NOT NULL COMMENT '생년월일',
    `email` VARCHAR(100) NOT NULL COMMENT '이메일',
    `phone` VARCHAR(15) NOT NULL COMMENT '전화번호',
    `address` VARCHAR(255) NOT NULL COMMENT '주소',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
    `enabled` INT DEFAULT 1 COMMENT '활성화여부'
);

drop TABLE IF EXISTS `pets`;
create Table `pets`(
    `no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '반려견 번호',
    `owner_no` BIGINT NOT NULL COMMENT  '반려견 주인 번호',
    `name` VARCHAR(50) NOT NULL COMMENT '반려견 이름',
    `species` VARCHAR(50) NOT NULL COMMENT '반려견 종',
    `size` VARCHAR(20) NOT NULL COMMENT '반려견 크기',
    `age` INT NOT NULL COMMENT '반려견 나이',
    `weight` FLOAT NOT NULL COMMENT '반려견 몸무게',
    `gender` VARCHAR(10) NOT NULL COMMENT '반려견 성별',
    `Neutered` VARCHAR(10) NOT NULL COMMENT '중성화 여부', 
    `profile_img` VARCHAR(255) DEFAULT NULL COMMENT '프로필 이미지',
    `vaccination` VARCHAR(255) NOT NULL COMMENT '예방접종 여부',
    `ect` TEXT NULL COMMENT '기타 사항',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '등록일자',
    `update_date` TIMESTAMP DEFAULT NOW() ON UPDATE NOW() COMMENT '수정일자',

    FOREIGN KEY (owner_no) REFERENCES users(no)     
    on update CASCADE 
    on delete CASCADE
);
 DROP Table IF EXISTS `hotelrooms`;

CREATE Table `hotelrooms`(
    `room_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '호텔 객실 번호',
    `room_type` VARCHAR(50) NOT NULL COMMENT '객실 종류',
    `room_price` INT NOT NULL COMMENT '가격',
    `etc` TEXT NULL COMMENT '세부 사항',
    `active` VARCHAR(10) NOT NULL COMMENT '예약 여부',
    `img` VARCHAR(255) NOT NULL COMMENT '객실 이미지'

);
 
 drop TABLE IF EXISTS `reservations`;

 CREATE TABLE `reservations`(
    `res_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '예약 번호',
    `user_no` BIGINT NOT NULL COMMENT '회원 번호',
    `pet_no` BIGINT NOT NULL COMMENT '반려견 번호',
    `room_no` BIGINT NOT NULL COMMENT '객실 번호',
    `res_date` DATE NOT NULL COMMENT '예약 날짜',
    `res_time` TIME NOT NULL COMMENT '예약 시간',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '예약일자',

    FOREIGN KEY (user_no) REFERENCES users(no)
    on update CASCADE
    on delete CASCADE,

    FOREIGN KEY (pet_no) REFERENCES pets(no)
    on update CASCADE
    on delete CASCADE,
    
    FOREIGN KEY (room_no) REFERENCES hotelrooms(room_no)
    on update CASCADE
    on delete CASCADE
 );

       

DROP Table IF EXISTS `hotelservices`;

CREATE Table `hotelservices`(
    `service_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '호텔 서비스 번호',
    `service_name` VARCHAR(100) NOT NULL COMMENT '서비스 이름',
    `description` TEXT NOT NULL COMMENT '서비스 설명',
    `service_price` INT NOT NULL COMMENT '가격'
);

DROP TABLE IF EXISTS `reservation_services`;
CREATE TABLE reservation_services (
    `rs_no` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '예약 서비스 번호',
    `res_no` BIGINT NOT NULL COMMENT '예약 번호',
    `service_no` BIGINT NOT NULL COMMENT '서비스 번호',
    FOREIGN KEY (`res_no`) REFERENCES reservations(res_no) ON DELETE CASCADE,
    FOREIGN KEY (`service_no`) REFERENCES hotelservices(service_no)
);

DROP TABLE IF EXISTS `notices`;

CREATE TABLE `notices`(
    `notice_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '공지사항 번호',
    `title` VARCHAR(200) NOT NULL COMMENT '공지사항 제목',
    `content` TEXT NOT NULL COMMENT '공지사항 내용',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '등록일자',
    `update_date` TIMESTAMP DEFAULT NOW() ON UPDATE NOW() COMMENT '수정일자'
);


DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth` (
    `auth_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '권한번호',
    `id` VARCHAR(36) NOT NULL COMMENT '사용자ID (UK)',
    `username` VARCHAR(50) NOT NULL COMMENT '사용자아이디',
    `auth` VARCHAR(100) NOT NULL COMMENT '권한 (ROLE_USER, ROLE_ADMIN, ...)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시'
);

DROP TABLE IF EXISTS `trainers`;
CREATE Table `trainers`(
    `trainer_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '트레이너 번호',
    `name` VARCHAR(50) NOT NULL COMMENT '트레이너 이름',
    `detail` TEXT NOT NULL COMMENT '트레이너 소개',
    `gender` VARCHAR(10) NOT NULL COMMENT '트레이너 성별',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '등록일자'
);

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`(
    `product_no` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '상품 번호',
    `name` VARCHAR(100) NOT NULL COMMENT '상품 이름',
    `description` TEXT NOT NULL COMMENT '상품 설명',
    `price` INT NOT NULL COMMENT '가격',
    `stock` INT NOT NULL COMMENT '재고',
    `category` VARCHAR(50) NOT NULL COMMENT '카테고리',
    `img` VARCHAR(255) DEFAULT NULL COMMENT '상품 이미지',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '등록일자'
);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `order_no` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 번호',
    `user_no` BIGINT NOT NULL COMMENT '회원 번호',
    `total_price` INT NOT NULL COMMENT '총 금액',
    `status` VARCHAR(20) DEFAULT 'READY' COMMENT '주문 상태',
    `order_date` TIMESTAMP DEFAULT NOW() COMMENT '주문일자',
    FOREIGN KEY (`user_no`) REFERENCES users(no)
);
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
    `item_no` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 상품 번호',
    `order_no` BIGINT NOT NULL COMMENT '주문 번호',
    `product_no` BIGINT NOT NULL COMMENT '상품 번호',
    `quantity` INT NOT NULL COMMENT '수량',
    `price` INT NOT NULL COMMENT '주문 당시 가격',
    FOREIGN KEY (`order_no`) REFERENCES orders(order_no) ON DELETE CASCADE,
    FOREIGN KEY (`product_no`) REFERENCES product(product_no)
); 


SET FOREIGN_KEY_CHECKS = 1;


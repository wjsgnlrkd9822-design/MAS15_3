-- Active: 1767840691076@@127.0.0.1@3306 @aloha
SET FOREIGN_KEY_CHECKS = 0;


SELECT * FROM hotelrooms

 drop TABLE IF EXISTS `reservations`;

 CREATE TABLE `reservations`(
    `res_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '예약 번호',
    `user_no` BIGINT NOT NULL COMMENT '회원 번호',
    `pet_no` BIGINT NOT NULL COMMENT '반려견 번호',
    `room_no` BIGINT NOT NULL COMMENT '객실 번호',
    `res_date` DATE NOT NULL COMMENT '체크인 날짜',
    `checkout_date` DATE NOT NULL COMMENT '체크아웃 날짜',
    `total_price` int NOT NULL COMMENT '총 가격',
    `res_time` TIME NOT NULL COMMENT '예약 시간',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '예약일자',
    `status` VARCHAR(20) NOT NULL DEFAULT '예약중' COMMENT '예약상태 (예약중/완료/취소)',  /* 예약 추가 */

    FOREIGN KEY (user_no) REFERENCES users(no)
    on update CASCADE
    on delete CASCADE,

    FOREIGN KEY (pet_no) REFERENCES pets(no)
    on update CASCADE
    on delete CASCADE,
    
    FOREIGN KEY (room_no) REFERENCES hotelrooms(room_no)
    on update CASCADE
    on delete CASCADE,

    INDEX idx_check_dates (res_date, checkout_date),    /* 예약 추가 */
    INDEX idx_room_status (room_no, status)             /* 예약 추가 */
);

 SELECT * FROM hotelrooms;


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
    `detail_address` VARCHAR(255) NOT NULL COMMENT '상세주소',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    `enabled` INT DEFAULT 1 COMMENT '활성화여부'
);

drop TABLE IF EXISTS `pets`;
create Table `pets`(
    `no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '반려견 번호',
    `owner_no` BIGINT NOT NULL COMMENT  '반려견 주인 번호',
    `name` VARCHAR(50) NOT NULL COMMENT '반려견 이름',
    `profile_img` LONGBLOB DEFAULT NULL COMMENT '프로필 이미지',
    `species` VARCHAR(50) NOT NULL COMMENT '반려견 종',
    `size` VARCHAR(20) NOT NULL COMMENT '반려견 크기',
    `age` INT NOT NULL COMMENT '반려견 나이',
    `weight` FLOAT NOT NULL COMMENT '반려견 몸무게',
    `gender` ENUM('수컷','암컷') NOT NULL COMMENT '반려견 성별',
    `neutered` ENUM('예','아니오') NOT NULL COMMENT '중성화 여부',
    `vaccination` VARCHAR(255) NOT NULL COMMENT '예방접종 여부',
    `certificate_file` MEDIUMBLOB COMMENT '건강 증명서 이미지 파일',
    `etc` TEXT NULL COMMENT '기타 사항',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    FOREIGN KEY (owner_no) REFERENCES users(no)     
    on update CASCADE 
    on delete CASCADE
);

SET FOREIGN_KEY_CHECKS = 0;
 DROP Table IF EXISTS `hotelrooms`;

CREATE Table `hotelrooms`(
    `room_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '호텔 객실 번호',
    `room_type` VARCHAR(50) NOT NULL COMMENT '객실 종류',
    `room_price` INT NOT NULL COMMENT '가격',
    `etc` TEXT NULL COMMENT '세부 사항',
    `active` VARCHAR(20) NOT NULL DEFAULT '예약가능' COMMENT '예약 여부',
    `img` VARCHAR(255) NOT NULL COMMENT '객실 이미지'

);

INSERT INTO hotelrooms (room_type, room_price, etc, active, img)
VALUES
('Large Dog', 110000, '대형견실', '예약가능', 'room_101.jpg'),
('Large Dog', 110000, '대형견실', '예약가능', 'room_102.jpg'),
('Large Dog', 110000, '대형견실', '예약가능', 'room_103.jpg'),
('Large Dog Deluxe', 140000, '대형견실(넓은공간)', '예약가능', 'room_104.jpg'),
('Medium Dog', 80000, '중형견실', '예약가능', 'room_201.jpg'),
('Medium Dog', 80000, '중형견실', '예약가능', 'room_202.jpg'),
('Medium Dog', 80000, '중형견실', '예약가능', 'room_203.jpg'),
('Medium Dog Deluxe', 100000, '중형견실(넓은공간)', '예약가능', 'room_204.jpg'),
('Medium Dog Deluxe', 100000, '중형견실(넓은공간)', '예약가능', 'room_205.jpg'),
('Small Dog', 50000, '소형견실', '예약가능', 'room_301.jpg'),
('Small Dog', 50000, '소형견실', '예약가능', 'room_302.jpg'),
('Small Dog', 50000, '소형견실', '예약가능', 'room_303.jpg'),
('Small Dog Deluxe', 70000, '소형견실(넓은공간)', '예약가능', 'room_304.jpg'),
('Small Dog Deluxe', 70000, '소형견실(넓은공간)', '예약가능', 'room_305.jpg');


SELECT * FROM hotelrooms

 

       

DROP Table IF EXISTS `hotelservices`;

CREATE Table `hotelservices`(
    `service_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '호텔 서비스 번호',
    `service_name` VARCHAR(100) NOT NULL COMMENT '서비스 이름',
    `description` TEXT NOT NULL COMMENT '서비스 설명',
    `service_price` INT NOT NULL COMMENT '가격'
);
INSERT INTO hotelservices (service_name, description, service_price)
VALUES
('그루밍 서비스', '전문 미용사의 목욕 및 미용 서비스', 30000),
('프리미엄 식사', '수제 영양식과 간식 제공', 15000),
('훈련 프로그램', '전문 트레이너와 1:1 교육', 40000),
('사진 촬영', '전문 포토그래퍼의 반려견 화보 촬영', 25000);




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
    `trainer_name` VARCHAR(50) NOT NULL COMMENT '트레이너 이름',
    `detail` TEXT NOT NULL COMMENT '트레이너 소개',
    `gender` VARCHAR(10) NOT NULL COMMENT '트레이너 성별',
    `img` VARCHAR(255) DEFAULT NULL COMMENT '트레이너 이미지',
    `reg_date` TIMESTAMP DEFAULT NOW() COMMENT '등록일자'
);


SET FOREIGN_KEY_CHECKS = 1;
/* 회원가입 후 생성한 아이디 집어넣어
    관리자 권한 부여
     */

INSERT INTO user_auth (id, username, auth)
SELECT id, username, 'ROLE_ADMIN'
FROM users
WHERE username = 'admin1234';  

INSERT INTO `user` (id, username, password, name, email)
VALUES (UUID(), 'admin', '123456', 
        '관리자', 'admin@naver.com');
                        
INSERT INTO `user_auth` (id, username, auth )
VALUES 
  ('admin1234', 'admin1234', 'ROLE_USER'),
  ('admin1234', 'admin1234', 'ROLE_ADMIN');
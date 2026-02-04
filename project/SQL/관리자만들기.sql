/* 회원가입 후 생성한 아이디 집어넣어
    관리자 권한 부여
     */

INSERT INTO user_auth (id, username, auth)
SELECT id, username, 'ROLE_ADMIN'
FROM users
WHERE username = '아이디';  

INSERT INTO `user` (id, username, password, name, email)
VALUES (UUID(), 'admin', '123456', 
        '관리자', 'admin@naver.com');
 
INSERT INTO `user_auth` (id, username, auth )
VALUES 
  ('admin123', 'admin123', 'ROLE_USER'),
  ('admin123', 'admin123', 'ROLE_ADMIN');
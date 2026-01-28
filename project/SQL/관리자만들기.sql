/* 회원가입 후 생성한 아이디 집어넣어
    관리자 권한 부여
     */

INSERT INTO user_auth (id, username, auth)
SELECT id, username, 'ROLE_ADMIN'
FROM users
WHERE username = '아이디';
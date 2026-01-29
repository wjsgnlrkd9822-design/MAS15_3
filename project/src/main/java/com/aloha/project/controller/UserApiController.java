package com.aloha.project.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aloha.project.dto.User;
import com.aloha.project.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController  // JSON 반환용
@RequestMapping("/api/users")  // API 경로
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    /**
     * 회원가입 처리 (비동기식)
     * [POST] - /api/users/join
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String,Object>> join(@RequestBody User user) {
        log.info(":::::::::: 회원 가입 처리 (비동기식) ::::::::::");
        log.info("user : " + user);

        Map<String,Object> response = new HashMap<>();
        try {
            int result = userService.join(user);

            if(result > 0) {
                response.put("success", true);
                response.put("message", "회원가입 성공!");
            } else {
                response.put("success", false);
                response.put("message", "회원가입 실패!");
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류 발생");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 아이디 중복 검사 (비동기식)
     * [GET] - /api/users/check/{username}
     */
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> userCheck(@PathVariable String username) throws Exception {
        log.info("아이디 중복 확인 : " + username);
        User user = userService.select(username);
        // 아이디 중복
        if( user != null ) {
            log.info("중복된 아이디 입니다 - " + username);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        // 사용 가능한 아이디입니다.
        log.info("사용 가능한 아이디 입니다." + username);
        return new ResponseEntity<>(user == null, HttpStatus.OK);
    }


    /**
     * 내 정보 조회 (비동기식)
     * [GET] - /api/users/select
     */
    @GetMapping("/select")
    public ResponseEntity<User> select(Principal principal) {
    try {
        String username = principal.getName();
        User user = userService.select(username);
        return ResponseEntity.ok(user);
    } catch(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

        

    /**
     * 내 정보 수정 (비동기식)
     * [PUT] - /api/users/update
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String,Object>> 
        update(@RequestBody User user, Principal principal) {
        Map<String,Object> response = new HashMap<>();

        try {
        // 로그인한 사용자 아이디
        String username = principal.getName();
        user.setUsername(username); // ⭐ 핵심

        int result = userService.update(user);

        response.put("success", result > 0);
        response.put("message", "정보 수정 완료");

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        e.printStackTrace();
        response.put("success", false);
        response.put("message", "서버 오류");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
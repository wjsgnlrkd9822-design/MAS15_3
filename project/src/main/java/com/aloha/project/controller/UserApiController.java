package com.aloha.project.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.project.dto.User;
import com.aloha.project.service.UserService;

import jakarta.validation.Valid;
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
    public ResponseEntity<Map<String,Object>> join(@Valid @RequestBody User user) {

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

    // @RequestBody로 전달된 JSON의 유효성 검사 실패를 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String,Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 아이디 중복 검사 (비동기식)
     * [GET] - /api/users/check/{username}
     */
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> userCheck(@PathVariable("username") String username) throws Exception {

       
        log.info("아이디 중복 확인 : " + username);
        User user = userService.select(username);
        
        // 사용 가능한 아이디 (user가 null이면 사용 가능)
        if (user == null) {
            log.info("사용 가능한 아이디 입니다: " + username);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        
        // 중복된 아이디
        log.info("중복된 아이디 입니다: " + username);
        return new ResponseEntity<>(false, HttpStatus.OK);
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


    /* 아이디 찾기 */
    @PostMapping("/findIdCheck")
    public ResponseEntity<Map<String,Object>> findIdCheck(@RequestBody Map<String,String> payload) {
        Map<String,Object> response = new HashMap<>();
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            String username = userService.findId(name, email);

            if(username != null){
                response.put("success", true);
                response.put("username", username);
            } else {
                response.put("success", false);
                response.put("message", "조회된 아이디가 없습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류 발생");
        }
        return ResponseEntity.ok(response);
    }

    
}
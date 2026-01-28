package com.aloha.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.project.dto.User;
import com.aloha.project.service.UserService;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

  

    /**
     * 회원 가입 화면
     * [GET] - /join
     * join.html
     * @return
     */
    @GetMapping("/join")
    public String join() {
        log.info(":::::::::: 회원 가입 화면 ::::::::::");
        return "login/join";
    }

    /**
     * 회원 가입 처리
     * [POST] - /join
     * ➡   ⭕ /login
     *      ❌ /join?error
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/join")
    public String joinPro(User user) throws Exception {
        log.info(":::::::::: 회원 가입 처리 ::::::::::");
        log.info("user : " + user);

        int result = userService.join(user);

        if( result > 0 ) {
            return "redirect:/login";
        }
				return "redirect:/join?error";
        
    }


    /**
     * 아이디 중복 검사
     * @param username
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> userCheck(@PathVariable("username") String username) throws Exception {
        log.info("아이디 중복 확인 : " + username);
        User user = userService.select(username);
        // 아이디 중복
        if( user != null ) {
            log.info("중복된 아이디 입니다 - " + username);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        // 사용 가능한 아이디입니다.
        log.info("사용 가능한 아이디 입니다." + username);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    

    /**
     * 로그인 화면
     * [GET] - /login
     * @return
     */
    @GetMapping("/login")
    public String login(
      @CookieValue(value = "remember-id", required = false) Cookie cookie,
      Model model
    ) {
      log.info(":::::::::: 로그인 화면 ::::::::::");
      String username = "";
      boolean rememberId = false;
      if( cookie != null ) {
        log.info("CookieName : " + cookie.getName());
        log.info("CookieValue : " + cookie.getValue());
        username = cookie.getValue();
        rememberId = true;
      }
      model.addAttribute("username", username);
      model.addAttribute("rememberId", rememberId);

      return "login/login";
    }
    
}

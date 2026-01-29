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

package com.aloha.project.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
                                    , HttpServletResponse response
                                    , Authentication authentication) throws ServletException, IOException {


      log.info("로그인 성공...");
      String rememberId = request.getParameter("remember-id"); 
      String username = request.getParameter("username");

      if( rememberId != null && rememberId.equals("on") ) {
        Cookie cookie = new Cookie("remember-id", username);
        cookie.setMaxAge(60 * 60 * 24 * 7);                       
        cookie.setPath("/");
        response.addCookie(cookie);
      }
      else {
        Cookie cookie = new Cookie("remember-id", username);
        cookie.setMaxAge(0);                    
        cookie.setPath("/");
        response.addCookie(cookie);
      }

      CustomUser customUser = (CustomUser) authentication.getPrincipal();                     
      User user = customUser.getUser();

      log.info("아이디 : " + user.getUsername());
      log.info("비밀번호 : " + user.getPassword());
      log.info("권한 : " + user.getAuthList());
      
      super.onAuthenticationSuccess(request, response, authentication);
    }

    
    
}

package com.aloha.project.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그아웃 성공 처리 이벤트 핸들러
 */
@Slf4j
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
  
  @Override
  public void onLogoutSuccess(HttpServletRequest request, 
                              HttpServletResponse response, 
                              Authentication authentication)
      throws IOException, ServletException {
    
    log.info("로그아웃 성공...");

    // 로그아웃 성공 후 리다이렉트할 경로 설정
    setDefaultTargetUrl("/");
    super.onLogoutSuccess(request, response, authentication);
  }

  
  
}

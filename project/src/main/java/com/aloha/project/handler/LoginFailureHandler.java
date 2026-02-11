package com.aloha.project.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 실패 처리자
 */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request
                                      , HttpServletResponse response
                                      , AuthenticationException exception) throws IOException, ServletException {
        log.info("로그인 처리 실패...");                                
        response.sendRedirect("/login?error");

    }
    
}

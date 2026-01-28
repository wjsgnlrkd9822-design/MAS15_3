package com.aloha.project.service;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserAuth;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    // 로그인
    public boolean login(User user, HttpServletRequest request) throws Exception;
    
    // 조회
    public User select(String username) throws Exception;
    
    // 회원 가입
    public int join(User user) throws Exception;

    // 회원 수정
    public int update(User user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;
    
}

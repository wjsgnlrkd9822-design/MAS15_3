package com.aloha.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserAuth;

@Mapper
public interface UserMapper {

    // 회원 조회
    public User select(String id) throws Exception;

    // 회원 가입
    public int join(User user) throws Exception;

    // 회원 수정
    public int update(User user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;
    
}

package com.aloha.project.service;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;

public interface UserSocialService {

    UserSocial selectByProviderAndSocialId(String provider, String socialId) throws Exception;
  
    UserSocial selectByUserNo(Long userNo) throws Exception;

    int insert(UserSocial userSocial) throws Exception;

    int deleteByUserNo(Long userNo) throws Exception;

    public User selectById(Long userNo) throws Exception;
}

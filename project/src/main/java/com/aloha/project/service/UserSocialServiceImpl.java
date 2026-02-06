package com.aloha.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;
import com.aloha.project.mapper.UserMapper;
import com.aloha.project.mapper.UserSocialMapper;

@Service
public class UserSocialServiceImpl implements UserSocialService  {
    
    @Autowired
    private UserSocialMapper userSocialMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserSocial selectByProviderAndSocialId(String provider, String socialId) throws Exception {
       return userSocialMapper.selectByProviderAndSocialId(provider, socialId);
    }

    @Override
    public UserSocial selectByUserNo(Long userNo) throws Exception {
        return userSocialMapper.selectByUserNo(userNo);
    }

    @Override
    public int insert(UserSocial userSocial) throws Exception {
        return userSocialMapper.insert(userSocial);
    }

    @Override
    public int deleteByUserNo(Long userNo) throws Exception {
        return userSocialMapper.deleteByUserno(userNo);
    }

    @Override
    public User selectById(Long userNo) throws Exception {
        return userMapper.selectByUserNo(userNo); 
    }
    
}

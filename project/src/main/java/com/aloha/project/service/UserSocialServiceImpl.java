package com.aloha.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;
import com.aloha.project.mapper.UserSocialMapper;

@Service
public class UserSocialServiceImpl implements UserSocialService {

    @Autowired
    private UserSocialMapper userSocialMapper;

    @Override
    public int insertSocial(UserSocial userSocial) throws Exception {
        return userSocialMapper.insertSocial(userSocial);
    }

    @Override
    public UserSocial selectSocial(UserSocial userSocial) throws Exception {
        return userSocialMapper.selectSocial(userSocial);
    }

    @Override
    public int updateSocial(UserSocial userSocial) throws Exception {
        return userSocialMapper.updateSocial(userSocial);
    }

    @Override
    public User selectBySocial(UserSocial userSocial) throws Exception {
        return userSocialMapper.selectBySocial(userSocial);
    }


}

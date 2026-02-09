package com.aloha.project.service;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;

public interface UserSocialService {

    public int insertSocial(UserSocial userSocial) throws Exception;

    public UserSocial selectSocial(UserSocial userSocial) throws Exception;
    
    public int updateSocial(UserSocial userSocial) throws Exception;

    public User selectBySocial(UserSocial userSocial) throws Exception;

}
